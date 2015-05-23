package org.agilewiki.console.profile;

import org.agilewiki.console.PostRequestBlade;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * A base role.
 */
public class ProfileRole implements Role {

    private Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    private Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();
    private ProfileBlade profileBlade;

    public ProfileRole(SimpleSimon simpleSimon)
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
        profileBlade = new ProfileBlade(simpleSimon);

        requests.put("logout", logoutBlade);
        requests.put("deleteAccount", deleteAccountBlade);
        requests.put("changePassword", changePasswordBlade);
        requests.put("changeEmailAddress", changeEmailAddressBlade);
        requests.put("newEmailAddress", newEmailAddressBlade);
        requests.put("profile", profileBlade);

        posts.put("logout", logoutBlade);
        posts.put("deleteAccount", deleteAccountBlade);
        posts.put("changePassword", changePasswordBlade);
        posts.put("newEmailAddress", newEmailAddressBlade);
        posts.put("changeEmailAddress", changeEmailAddressBlade);
    }

    @Override
    public String roleName() {
        return "profile";
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
        return profileBlade;
    }

    @Override
    public String getDefaultRequestPage() {
        return "profile";
    }

    @Override
    public String menu(HttpServletRequest request,
                       String page,
                       String setTimestamp,
                       String timestamp,
                       String setRole) {
        StringBuilder home = new StringBuilder();
        home.append("<a>Profile &#9660;</a>\n");
        home.append("<ul class=\"sub-menu\">\n");

        home.append("<li>");
        if ("profile".equals(page)) {
            home.append("<a>");
        } else {
            home.append("<a href=\"?from=");
            home.append(page);
            home.append("&to=profile");
            home.append(setTimestamp);
            home.append(setRole);
            home.append("#rupa\">");
        }
        home.append("My Profile");
        home.append("</a>");
        home.append("</li>\n");

        home.append("<li>");
        if ("changePassword".equals(page)) {
            home.append("<a>");
        } else {
            home.append("<a href=\"?from=");
            home.append(page);
            home.append("&to=changePassword");
            home.append(setTimestamp);
            home.append(setRole);
            home.append("#rupa\">");
        }
        home.append("Change Password");
        home.append("</a>");
        home.append("</li>\n");

        home.append("<li>");
        if ("changeEmailAddress".equals(page)) {
            home.append("<a>");
        } else {
            home.append("<a href=\"?from=");
            home.append(page);
            home.append("&to=changeEmailAddress");
            home.append(setTimestamp);
            home.append(setRole);
            home.append("#rupa\">");
        }
        home.append("Change Email Address");
        home.append("</a>");
        home.append("</li>\n");

        home.append("<li>");
        if ("deleteAccount".equals(page)) {
            home.append("<a>");
        } else {
            home.append("<a href=\"?from=");
            home.append(page);
            home.append("&to=deleteAccount");
            home.append(setTimestamp);
            home.append(setRole);
            home.append("#rupa\">");
        }
        home.append("Delete Account");
        home.append("</a>");
        home.append("</li>\n");

        home.append("</ul>\n");
        return home.toString();
    }
}
