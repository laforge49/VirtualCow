package org.agilewiki.console.user;

import org.agilewiki.console.PostRequestBlade;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;

import java.util.HashMap;
import java.util.Map;

/**
 * A base role.
 */
public class UserRole implements Role {

    private Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    private Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();
    private UserBlade userBlade;

    public UserRole(SimpleSimon simpleSimon)
            throws Exception {
        ChangeEmailAddressBlade changeEmailAddressBlade;
        ChangePasswordBlade changePasswordBlade;
        DeleteAccountBlade deleteAccountBlade;
        LogoutBlade logoutBlade;
        NewEmailAddressBlade newEmailAddressBlade;

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
        menuItem(home, currentPage, setTimestamp, setRole, "user", "User Home");
        menuItem(home, currentPage, setTimestamp, setRole, "changePassword", "Change Password");
        menuItem(home, currentPage, setTimestamp, setRole, "changeEmailAddress", "Change Email Address");
        menuItem(home, currentPage, setTimestamp, setRole, "deleteAccount", "Delete Account");
    }
}
