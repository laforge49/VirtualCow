package org.agilewiki.console.user;

import org.agilewiki.console.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A base role.
 */
public class UserRole extends RoleBase {

    private Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    private Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();
    private UserBlade userBlade;
    private ChangeEmailAddressBlade changeEmailAddressBlade;
    private ChangePasswordBlade changePasswordBlade;
    private DeleteAccountBlade deleteAccountBlade;
    private LogoutBlade logoutBlade;
    private NewEmailAddressBlade newEmailAddressBlade;

    public UserRole(SimpleSimon simpleSimon)
            throws Exception {
        super(simpleSimon);
        logoutBlade = new LogoutBlade(simpleSimon);
        deleteAccountBlade = new DeleteAccountBlade(simpleSimon);
        changePasswordBlade = new ChangePasswordBlade(simpleSimon);
        changeEmailAddressBlade = new ChangeEmailAddressBlade(simpleSimon);
        newEmailAddressBlade = new NewEmailAddressBlade(simpleSimon);
        userBlade = new UserBlade(simpleSimon);

        requests.put("logout", logoutBlade);
        requests.put("deleteAccount", deleteAccountBlade);
        requests.put("changePassword", changePasswordBlade);
        requests.put("changeEmailAddress", changeEmailAddressBlade);
        requests.put("newEmailAddress", newEmailAddressBlade);
        requests.put("profile", userBlade);

        posts.put("logout", logoutBlade);
        posts.put("deleteAccount", deleteAccountBlade);
        posts.put("changePassword", changePasswordBlade);
        posts.put("newEmailAddress", newEmailAddressBlade);
        posts.put("changeEmailAddress", changeEmailAddressBlade);

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
    public RequestBlade requestBlade(String page) {
        return requests.get(page);
    }

    @Override
    public PostRequestBlade postRequestBlade(String page) {
        return posts.get(page);
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
        menuItem(home, currentPage, setTimestamp, setRole, "user", userBlade.niceName());
        menuItem(home, currentPage, setTimestamp, setRole, "changePassword", changePasswordBlade.niceName());
        menuItem(home, currentPage, setTimestamp, setRole, "changeEmailAddress", changeEmailAddressBlade.niceName());
        menuItem(home, currentPage, setTimestamp, setRole, "deleteAccount", deleteAccountBlade.niceName());
    }
}
