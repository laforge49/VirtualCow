package org.agilewiki.vcow;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.GenerativeNode;
import org.agilewiki.awdb.db.ids.NameId;
import org.agilewiki.awdb.db.ids.ValueId;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.db.virtualcow.UnexpectedChecksumException;
import org.agilewiki.awdb.nodes.*;
import org.agilewiki.vcow.roles.Role;
import org.agilewiki.vcow.roles.VCRole_Node;
import org.agilewiki.vcow.roles.admin.Admin_Role;
import org.agilewiki.vcow.roles.developer.Developer_Role;
import org.agilewiki.vcow.roles.user.User_Role;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class VCUser_Node extends User_Node implements GenerativeNode {

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

    public static boolean servletInit() {
        ServletConfig servletConfig = SimpleSimon.simpleSimon.getServletConfig();
        String adminEmail = servletConfig.getInitParameter("adminEmail");
        String adminPassword = servletConfig.getInitParameter("adminPassword");
        if (adminEmail == null || adminPassword == null) {
            return false;
        }
        ServletContext servletContext = servletConfig.getServletContext();
        String userId = AwDb.randomId.generate();
        String passwordHash = VCUser_Node.encodePassword(
                servletContext, userId, adminPassword);
        if (passwordHash == null) {
            servletContext.log("unable to hash password");
            return false;
        }
        String emailId = ValueId.generate(adminEmail);
        String error = VCUser_Node.createUser(userId,
                emailId,
                passwordHash,
                VCRole_Node.roleName(User_Role.ID),
                VCRole_Node.roleName(Developer_Role.ID),
                VCRole_Node.roleName(Admin_Role.ID));
        if (error == null) {
            getAwDb().createLnk1(userId, Lnk1_NodeFactory.MEMBER_OF_ID, Group_NodeFactory.ADMINS_GROUP_ID);
            return true;
        }
        servletConfig.getServletContext().log(error);
        return false;
    }

    public static String createUser(String userId,
                                    String emailId,
                                    String passwordHash,
                                    String... userRoles) {
        AwDb awDb = SimpleSimon.simpleSimon.awDb;
        if (awDb.keyHasTargetId(NameIds.EMAIL_ID, emailId, awDb.getDbTimestamp())) {
            return "duplicate email: " + ValueId.value(emailId);
        }
        VCUser_Node user_node = new VCUser_Node(null, FactoryRegistry.MAX_TIMESTAMP);
        user_node.createNode(userId, User_NodeFactory.ID, userId);
        user_node.set(NameId.SUBJECT, emailId);
        user_node.set(NameIds.PASSWORD_KEY, passwordHash);
        user_node.createSecondaryId(NameIds.EMAIL_ID, emailId);
        for (String userRole : userRoles) {
            user_node.createSecondaryId(NameIds.ROLE_ID, NameIds.generate(userRole));
        }
        user_node.createLnk1(Lnk1_NodeFactory.MEMBER_OF_ID, Group_NodeFactory.USERS_GROUP_ID);
        awDb.addNode(user_node);
        return null;
    }

    public VCUser_Node(String nodeId, long timestamp) {
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
