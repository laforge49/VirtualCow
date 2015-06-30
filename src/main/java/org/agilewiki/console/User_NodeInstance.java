package org.agilewiki.console;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.NodeBase;
import org.agilewiki.awdb.nodes.Key_NodeFactory;
import org.agilewiki.console.roles.Role;
import org.agilewiki.console.roles.Role_NodeInstance;
import org.agilewiki.console.roles.admin.Admin_Role;
import org.agilewiki.console.roles.developer.Developer_Role;
import org.agilewiki.console.roles.user.User_Role;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class User_NodeInstance extends NodeBase {

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

    public static String userId(String email, long timestamp) {
        while (true) {
            try {
                String emailId = ValueId.generate(email);
                return getAwDb().getKeyTargetId(NameIds.EMAIL_ID, emailId, timestamp);
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public static String createUser(String userId,
                                    String emailId,
                                    String passwordHash,
                                    String... userRoles) {
        AwDb awDb = SimpleSimon.simpleSimon.awDb;
        if (awDb.keyHasTargetId(NameIds.EMAIL_ID, emailId, awDb.getDbTimestamp())) {
            return "duplicate email: " + ValueId.value(emailId);
        }
        awDb.set(userId, "$nsubject", emailId);
        awDb.set(userId, NameIds.PASSWORD_KEY, passwordHash);
        awDb.createSecondaryId(userId, NameIds.EMAIL_ID, emailId);
        awDb.createSecondaryId(userId, Key_NodeFactory.NODETYPE_ID, User_Node.ID);
        for (String userRole : userRoles) {
            awDb.createSecondaryId(userId, NameIds.ROLE_ID, NameIds.generate(userRole));
        }
        return null;
    }

    public static boolean init() {
        ServletConfig servletConfig = SimpleSimon.simpleSimon.getServletConfig();
        String adminEmail = servletConfig.getInitParameter("adminEmail");
        String adminPassword = servletConfig.getInitParameter("adminPassword");
        if (adminEmail == null || adminPassword == null) {
            return false;
        }
        ServletContext servletContext = servletConfig.getServletContext();
        String userId = AwDb.randomId.generate();
        String passwordHash = User_NodeInstance.encodePassword(
                servletContext, userId, adminPassword);
        if (passwordHash == null) {
            servletContext.log("unable to hash password");
            return false;
        }
        String emailId = ValueId.generate(adminEmail);
        String error = User_NodeInstance.createUser(userId,
                emailId,
                passwordHash,
                Role_NodeInstance.roleName(User_Role.ID),
                Role_NodeInstance.roleName(Developer_Role.ID),
                Role_NodeInstance.roleName(Admin_Role.ID));
        if (error == null)
            return true;
        servletConfig.getServletContext().log(error);
        return false;
    }

    public User_NodeInstance(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    public List<Role> roles() {
        while (true) {
            try {
                List<Role> roles = new ArrayList<Role>();
                for (String roleId : nodeValueIdIterable(NameIds.ROLE_ID)) {
                    Role role = SimpleSimon.simpleSimon.roles.get(NameId.name(roleId));
                    if (role != null)
                        roles.add(role);
                }
                return roles;
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public boolean hasRole(String role) {
        while (true) {
            try {
                String roleId = NameId.generate(role);
                return nodeHasValueId(NameIds.ROLE_ID, roleId);
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public String getEmailAddress() {
        while (true) {
            try {
                return getNodeValue(NameIds.EMAIL_ID);
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public String passwordDigest() {
        while (true) {
            try {
                return (String) get(NameIds.PASSWORD_KEY);
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public boolean confirmPassword(ServletContext servletContext,
                                   String password) {
        String passwordDigest = passwordDigest();
        if (passwordDigest == null)
            return false;
        return passwordDigest.equals(encodePassword(servletContext, getNodeId(), password));
    }
}
