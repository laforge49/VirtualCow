package org.agilewiki.console.oodb.nodes.roles.user;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.nodes.roles.RoleBase;
import org.agilewiki.console.oodb.nodes.roles.user.changePassword.ChangePasswordBlade;
import org.agilewiki.console.oodb.nodes.roles.user.delete.DeleteAccountBlade;
import org.agilewiki.console.oodb.nodes.roles.user.logout.LogoutBlade;
import org.agilewiki.console.oodb.nodes.roles.user.newEmailAddress.NewEmailAddressBlade;

import java.util.Map;

/**
 * A base role.
 */
public class UserRole extends RoleBase {

    private UserBlade userBlade;
    private ChangeEmailAddressBlade changeEmailAddressBlade;
    private ChangePasswordBlade changePasswordBlade;
    private DeleteAccountBlade deleteAccountBlade;
    private LogoutBlade logoutBlade;
    private NewEmailAddressBlade newEmailAddressBlade;

    public UserRole(SimpleSimon simpleSimon, String roleId, Map<String, String> parameters)
            throws Exception {
        super(simpleSimon, roleId, parameters);

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
