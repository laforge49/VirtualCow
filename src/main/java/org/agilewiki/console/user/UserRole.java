package org.agilewiki.console.user;

import org.agilewiki.console.*;

import java.util.HashMap;
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

    public UserRole(SimpleSimon simpleSimon)
            throws Exception {
        super(simpleSimon);
        logoutBlade = new LogoutBlade(this, "logout");
        requests.put(logoutBlade.page, logoutBlade);
        posts.put(logoutBlade.page, logoutBlade);

        deleteAccountBlade = new DeleteAccountBlade(this, "deleteAccount");
        requests.put(deleteAccountBlade.page, deleteAccountBlade);
        posts.put(deleteAccountBlade.page, deleteAccountBlade);

        changePasswordBlade = new ChangePasswordBlade(this, "changePassword");
        requests.put(changePasswordBlade.page, changePasswordBlade);
        posts.put(changePasswordBlade.page, changePasswordBlade);

        changeEmailAddressBlade = new ChangeEmailAddressBlade(this, "changeEmailAddress");
        requests.put(changeEmailAddressBlade.page, changeEmailAddressBlade);
        posts.put(changeEmailAddressBlade.page, changeEmailAddressBlade);

        newEmailAddressBlade = new NewEmailAddressBlade(this, "newEmailAddress");
        requests.put(newEmailAddressBlade.page, newEmailAddressBlade);
        posts.put(newEmailAddressBlade.page, newEmailAddressBlade);

        userBlade = new UserBlade(this, "user");
        requests.put(userBlade.page, userBlade);

        simpleSimon.db.registerTransaction(RecreateUserRoleTransaction.NAME, RecreateUserRoleTransaction.class);
        RecreateUserRoleTransaction.userRole = this;
    }

    @Override
    public SimpleSimon simpleSimon() {
        return simpleSimon;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateUserRoleTransaction.NAME;
    }

    @Override
    public String roleName() {
        return "user";
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return userBlade;
    }

    @Override
    public String getDefaultRequestPage() {
        return "user";
    }

    @Override
    public String niceRoleName() {
        return "User";
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
