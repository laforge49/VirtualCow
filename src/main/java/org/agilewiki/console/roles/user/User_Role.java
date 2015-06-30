package org.agilewiki.console.roles.user;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.roles.Role_NodeInstance;
import org.agilewiki.console.roles.user.changePassword.ChangePasswordBlade;
import org.agilewiki.console.roles.user.delete.DeleteAccountBlade;
import org.agilewiki.console.roles.user.logout.LogoutBlade;
import org.agilewiki.console.roles.user.newEmailAddress.NewEmailAddressBlade;
import org.agilewiki.utils.immutable.FactoryRegistry;

/**
 * A base role.
 */
public class User_Role extends Role_NodeInstance {
    public final static String ID = "$nuser.role";

    public static void create(AwDb awDb)
            throws Exception {
        awDb.addTimelessNode(new User_Role(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    private UserBlade userBlade;
    private ChangeEmailAddressBlade changeEmailAddressBlade;
    private ChangePasswordBlade changePasswordBlade;
    private DeleteAccountBlade deleteAccountBlade;
    private LogoutBlade logoutBlade;
    private NewEmailAddressBlade newEmailAddressBlade;

    public User_Role(String nodeId, long timestamp)
            throws Exception {
        super(nodeId, timestamp);
        niceRoleName = "User";
        logoutBlade = new LogoutBlade(this, "logout");
        deleteAccountBlade = new DeleteAccountBlade(this, "deleteAccount");
        changePasswordBlade = new ChangePasswordBlade(this, "changePassword");
        changeEmailAddressBlade = new ChangeEmailAddressBlade(this, "changeEmailAddress");
        newEmailAddressBlade = new NewEmailAddressBlade(this, "newEmailAddress");
        userBlade = new UserBlade(this, "user");

        RecreateUserRole_NodeFactory.create(getAwDb());
        getAwDb().registerTransaction(RecreateUserRole_Node.NAME, RecreateUserRole_Node.class);
        RecreateUserRole_Node.userRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateUserRole_Node.NAME;
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
