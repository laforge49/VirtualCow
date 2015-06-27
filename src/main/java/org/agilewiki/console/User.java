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
                for (String roleId : simpleSimon.ooDb.keyValueIdIterable(userId, ROLE_ID, FactoryRegistry.MAX_TIMESTAMP)) {
                    Role role = simpleSimon.roles.get(NameId.name(roleId));
                    if (role != null)
                        roles.add(role);
                }
                return roles;
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public static boolean hasRole(String userId, String role) {
        OODb ooDb = SimpleSimon.simpleSimon.ooDb;
        while (true) {
            try {
                String roleId = NameId.generate(role);
                return ooDb.hasKeyValue(userId, ROLE_ID, roleId, FactoryRegistry.MAX_TIMESTAMP);
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public static String email(String userId, long timestamp) {
        while (true) {
            try {
                return SimpleSimon.simpleSimon.ooDb.getKeyValue(userId, EMAIL_ID, timestamp);
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public static String userId(String email, long timestamp) {
        while (true) {
            try {
                String emailId = ValueId.generate(email);
                return SimpleSimon.simpleSimon.ooDb.getKeyTarget(EMAIL_ID, emailId, timestamp);
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public static boolean confirmPassword(ServletContext servletContext,
                                          String userId,
                                          String password) {
        String passwordDigest = passwordDigest(userId, FactoryRegistry.MAX_TIMESTAMP);
        if (passwordDigest == null)
            return false;
        return passwordDigest.equals(encodePassword(servletContext, userId, password));
    }

    public static String passwordDigest(String userId, long timestamp) {
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

    public static boolean init() {
        ServletConfig servletConfig = SimpleSimon.simpleSimon.getServletConfig();
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
        String error = createUser(userId,
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

    public static String createUser(String userId,
                                    String emailId,
                                    String passwordHash,
                                    String... userRoles) {
        Db db = SimpleSimon.simpleSimon.db;
        OODb ooDb = SimpleSimon.simpleSimon.ooDb;
        if (ooDb.hasKeyTarget(EMAIL_ID, emailId, db.getTimestamp())) {
            return "duplicate email: " + ValueId.value(emailId);
        }
        ooDb.set(userId, "$nsubject", emailId);
        ooDb.set(userId, PASSWORD_KEY, passwordHash);
        ooDb.createSecondaryId(userId, EMAIL_ID, emailId);
        ooDb.createSecondaryId(userId, Key_Node.NODETYPE_ID, User_Node.ID);
        for (String userRole : userRoles) {
            ooDb.createSecondaryId(userId, ROLE_ID, NameIds.generate(userRole));
        }
        return null;
    }
}
