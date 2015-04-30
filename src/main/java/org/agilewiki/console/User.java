package org.agilewiki.console;

import org.agilewiki.console.transactions.BadUserAddressTransaction;
import org.agilewiki.console.transactions.BadUserPasswordTransaction;
import org.agilewiki.console.transactions.LoginTransaction;
import org.agilewiki.console.transactions.LogoutTransaction;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.virtualcow.Db;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Define a user.
 */
public class User {
    public static final String EMAIL_ID = NameId.generate("email");
    public static final String USER_TYPE_ID = NameId.generate("userType");
    public static final String PASSWORD_KEY = NameId.generate("password");
    public static final String ADMIN_USER_ID = NameId.generate("adminUser");
    public static final String USER_KEY = NameId.generate("user");

    public static String createUser(Db db,
                                    ServletContext servletContext,
                                    String email,
                                    String password,
                                    String userTypeId) {
        String emailId = ValueId.generate(email);
        String emailSecondaryId = SecondaryIds.secondaryId(EMAIL_ID, emailId);
        for (String userId : SecondaryIds.vmnIdIterable(db, emailSecondaryId, db.getTimestamp())) {
            return "duplicate email: " + email;
        }
        String userId = RandomIds.randomId.generate();

        String passwordHash = encodePassword(servletContext, userId, password);
        if (passwordHash == null) {
            servletContext.log("unable to hash password");
            return "unable to hash password";
        }
        db.set(userId, PASSWORD_KEY, passwordHash);
        SecondaryIds.createSecondaryId(db, userId, emailSecondaryId);
        String userTypeSecondaryId = SecondaryIds.secondaryId(USER_TYPE_ID, userTypeId);
        SecondaryIds.createSecondaryId(db, userId, userTypeSecondaryId);
        return null;
    }

    public static String email(Db db, String userId, long timestamp) {
        String emailSecondaryInv = SecondaryIds.secondaryInv(userId, EMAIL_ID);
        for (String emailId : db.keysIterable(emailSecondaryInv, timestamp)) {
            return ValueId.value(emailId);
        }
        return null;
    }

    public static String login(Db db,
                               ServletContext servletContext,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               String email,
                               String password) {
        String emailId = ValueId.generate(email);
        String emailSecondaryId = SecondaryIds.secondaryId(EMAIL_ID, emailId);
        for (String userId : SecondaryIds.vmnIdIterable(db, emailSecondaryId, db.getTimestamp())) {
            return login2(db, servletContext, request, response, email, password, userId);
        }
        try {
            new BadUserAddressTransaction().update(db, email, request);
        } catch (Exception e) {
            servletContext.log("Update failure", e);
        }
        return "Invalid email address / password";
    }

    static String login2(Db db,
                         ServletContext servletContext,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         String email,
                         String password,
                         String userId) {
        if (!confirmPassword(db, servletContext, userId, password)) {
            try {
                new BadUserPasswordTransaction().update(db, email, request, userId);
            } catch (Exception e) {
                servletContext.log("Update failure", e);
            }
            return "Invalid email address / password";
        }
        Cookie loginCookie = new Cookie("userId", userId);
        loginCookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(loginCookie);
        try {
            new LoginTransaction().update(db, email, request, userId);
        } catch (Exception e) {
            servletContext.log("failed update", e);
        }
        return null;
    }

    public static boolean confirmPassword(Db db,
                                   ServletContext servletContext,
                                   String userId,
                                   String password) {
        String storedPassword = (String) db.get(userId, PASSWORD_KEY, db.getTimestamp());
        return storedPassword.equals(encodePassword(servletContext, userId, password));
    }

    public static String encodePassword(ServletContext servletContext, String userId, String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            servletContext.log("no such algorithm: SHA-256");
            return null;
        }
        String digest = bytesToHex(md.digest((userId + password).getBytes()));
        return digest;
    }

final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static boolean init(Db db, ServletConfig servletConfig) {
        String adminEmail = servletConfig.getInitParameter("adminEmail");
        String adminPassword = servletConfig.getInitParameter("adminPassword");
        if (adminEmail == null || adminPassword == null) {
            return false;
        }
        String error = createUser(db,
                servletConfig.getServletContext(),
                adminEmail,
                adminPassword,
                ADMIN_USER_ID);
        if (error == null)
            return true;
        servletConfig.getServletContext().log(error);
        return false;
    }
}
