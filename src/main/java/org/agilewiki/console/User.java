package org.agilewiki.console;

import org.agilewiki.console.transactions.BadUserAddressTransaction;
import org.agilewiki.console.transactions.BadUserPasswordTransaction;
import org.agilewiki.console.transactions.LoginTransaction;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.mail.MessagingException;
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
    public static final String GUEST_USER_ID = NameId.generate("guestUser");
    public static final String USER_KEY = NameId.generate("user");

    public static String email(Db db, String userId, long timestamp) {
        while (true) {
            try {
                String emailSecondaryInv = SecondaryIds.secondaryInv(userId, EMAIL_ID);
                for (String emailId : db.keysIterable(emailSecondaryInv, timestamp)) {
                    return ValueId.value(emailId);
                }
                return null;
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public static String userId(Db db, String email, long timestamp) {
        while (true) {
            try {
                String emailId = ValueId.generate(email);
                String emailSecondaryId = SecondaryIds.secondaryId(EMAIL_ID, emailId);
                for (String userId : db.keysIterable(emailSecondaryId, timestamp)) {
                    return userId;
                }
                return null;
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public static boolean send(Db db, ServletContext servletContext, String userId, String subject, String body) {
        String email = email(db, userId, FactoryRegistry.MAX_TIMESTAMP);
        boolean sent = true;
        try {
            sent = MailOut.send(email, subject, body);
        } catch (MessagingException me) {
            sent = false;
            servletContext.log("unable to send to " + email, me);
        }
        return sent;
    }

    public static boolean confirmPassword(Db db,
                                          ServletContext servletContext,
                                          String userId,
                                          String password) {
        String passwordDigest = passwordDigest(db, userId, FactoryRegistry.MAX_TIMESTAMP);
        if (passwordDigest == null)
            return false;
        return passwordDigest.equals(encodePassword(servletContext, userId, password));
    }

    public static String passwordDigest(Db db, String userId, long timestamp) {
        while (true) {
            try {
                return (String) db.get(userId, PASSWORD_KEY, timestamp);
            } catch (UnexpectedChecksumException uce) {
            }
        }
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
        ServletContext servletContext = servletConfig.getServletContext();
        String userId = RandomIds.randomId.generate();
        String passwordHash = encodePassword(
                servletContext, userId, adminPassword);
        if (passwordHash == null) {
            servletContext.log("unable to hash password");
            return false;
        }
        String emailId = ValueId.generate(adminEmail);
        String error = createUser(db,
                userId,
                emailId,
                passwordHash,
                ADMIN_USER_ID);
        if (error == null)
            return true;
        servletConfig.getServletContext().log(error);
        return false;
    }

    public static String createUser(Db db,
                                    String userId,
                                    String emailId,
                                    String passwordHash,
                                    String userTypeId) {
        String emailSecondaryId = SecondaryIds.secondaryId(EMAIL_ID, emailId);
        for (String uId : SecondaryIds.vmnIdIterable(db, emailSecondaryId, db.getTimestamp())) {
            return "duplicate email: " + ValueId.value(emailId);
        }

        db.set(userId, PASSWORD_KEY, passwordHash);
        SecondaryIds.createSecondaryId(db, userId, emailSecondaryId);
        String userTypeSecondaryId = SecondaryIds.secondaryId(USER_TYPE_ID, userTypeId);
        SecondaryIds.createSecondaryId(db, userId, userTypeSecondaryId);
        return null;
    }
}
