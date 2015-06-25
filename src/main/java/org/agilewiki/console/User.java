package org.agilewiki.console;

import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.Key_Node;
import org.agilewiki.console.oodb.nodes.User_Node;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.console.oodb.nodes.roles.admin.Admin_Role;
import org.agilewiki.console.oodb.nodes.roles.developer.Developer_Role;
import org.agilewiki.console.oodb.nodes.roles.user.User_Role;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.mail.MessagingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Define a user.
 */
public class User {
    public static final String EMAIL_ID = NameId.generate("email");
    public static final String ROLE_ID = NameId.generate("role");
    public static final String PASSWORD_KEY = NameId.generate("password");
    public static final String USER_KEY = NameId.generate("user");

    public static List<Role> roles(SimpleSimon simpleSimon, String userId) {
        while (true) {
            try {
                List<Role> roles = new ArrayList<Role>();
                String inv = SecondaryIds.secondaryInv(userId, ROLE_ID);
                for (String roleId : simpleSimon.db.keysIterable(inv, FactoryRegistry.MAX_TIMESTAMP)) {
                    Role role = simpleSimon.roles.get(NameId.name(roleId));
                    if (role != null)
                        roles.add(role);
                }
                return roles;
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public static boolean hasRole(Db db, String userId, String role) {
        OODb ooDb = SimpleSimon.simpleSimon.ooDb;
        while (true) {
            try {
                String inv = SecondaryIds.secondaryInv(userId, ROLE_ID);
                String roleId = NameId.generate(role);
                return ooDb.get(inv, roleId, FactoryRegistry.MAX_TIMESTAMP) != null;
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

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
                for (String userId : SecondaryId.vmnIdIterable(db, emailSecondaryId, timestamp)) {
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
        OODb ooDb = SimpleSimon.simpleSimon.ooDb;
        while (true) {
            try {
                return (String) ooDb.get(userId, PASSWORD_KEY, timestamp);
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

    public static boolean init(SimpleSimon simpleSimon) {
        Db db = simpleSimon.db;
        ServletConfig servletConfig = simpleSimon.getServletConfig();
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
                User_Role.get().roleName(),
                Developer_Role.get().roleName(),
                Admin_Role.get().roleName());
        if (error == null)
            return true;
        servletConfig.getServletContext().log(error);
        return false;
    }

    public static String createUser(Db db,
                                    String userId,
                                    String emailId,
                                    String passwordHash,
                                    String... userRoles) {
        OODb ooDb = SimpleSimon.simpleSimon.ooDb;
        String emailSecondaryId = SecondaryIds.secondaryId(EMAIL_ID, emailId);
        for (String uId : SecondaryIds.vmnIdIterable(db, emailSecondaryId, db.getTimestamp())) {
            return "duplicate email: " + ValueId.value(emailId);
        }
        ooDb.set(userId, "$nsubject", emailId);
        ooDb.set(userId, PASSWORD_KEY, passwordHash);
        ooDb.createSecondaryId(userId, emailSecondaryId);
        ooDb.createSecondaryId(userId,
                SecondaryId.secondaryId(Key_Node.NODETYPE_ID, User_Node.ID));
        for (String userRole : userRoles) {
            String userTypeSecondaryId =
                    SecondaryIds.secondaryId(ROLE_ID, NameIds.generate(userRole));
            ooDb.createSecondaryId(userId, userTypeSecondaryId);
        }
        return null;
    }
}
