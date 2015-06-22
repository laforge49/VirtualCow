package org.agilewiki.console.oodb.nodes.roles.user;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;
import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.user.changePassword.ChangePasswordBlade;
import org.agilewiki.console.oodb.nodes.roles.user.delete.DeleteAccountBlade;
import org.agilewiki.console.oodb.nodes.roles.user.logout.LogoutBlade;
import org.agilewiki.console.oodb.nodes.roles.user.newEmailAddress.NewEmailAddressBlade;

/**
 * A base role.
 */
public class UserRole_Node extends Role_NodeInstance {
    private static UserRole_Node userRole_node;
    public final static String ID = "$nuserRole.node";

    public static UserRole_Node get() {
        return userRole_node;
    }

    public static void create()
            throws Exception {
        userRole_node = new UserRole_Node(ID, Role_Node.ID);
    }

    private UserBlade userBlade;
    private ChangeEmailAddressBlade changeEmailAddressBlade;
    private ChangePasswordBlade changePasswordBlade;
    private DeleteAccountBlade deleteAccountBlade;
    private LogoutBlade logoutBlade;
    private NewEmailAddressBlade newEmailAddressBlade;

    public UserRole_Node(String nodeId, String factoryId)
            throws Exception {
        super(nodeId, factoryId);
        niceRoleName = "User";
        logoutBlade = new LogoutBlade(this, "logout");
        deleteAccountBlade = new DeleteAccountBlade(this, "deleteAccount");
        changePasswordBlade = new ChangePasswordBlade(this, "changePassword");
        changeEmailAddressBlade = new ChangeEmailAddressBlade(this, "changeEmailAddress");
        newEmailAddressBlade = new NewEmailAddressBlade(this, "newEmailAddress");
        userBlade = new UserBlade(this, "user");

        RecreateUserRole_Node.create();
        simpleSimon.db.registerTransaction(RecreateUserRole_NodeInstance.NAME, RecreateUserRole_NodeInstance.class);
        RecreateUserRole_NodeInstance.userRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateUserRole_NodeInstance.NAME;
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return userBlade;
    }

    @Override
    public void menuItems(StringBuilder home,
                          String currentPage,
                          String setTimestamp,
                          String timestamp,
                          String setRole) {
        menuItem(home, currentPage, setTimestamp, setRole, userBlade);
        menuItem(home, currentPage, setTimestamp, setRole, changePasswordBlade);
        menuItem(home, currentPage, setTimestamp, setRole, changeEmailAddressBlade);
        menuItem(home, currentPage, setTimestamp, setRole, deleteAccountBlade);
    }
}
