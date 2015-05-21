package org.agilewiki.console.maintenance;

import org.agilewiki.console.PostRequestBlade;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.profile.ProfileRole;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * The maintenance role.
 */
public class MaintenanceRole implements Role {

    private final ProfileRole profileRole;
    private Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    private Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();
    private HomeBlade homeBlade;

    public MaintenanceRole(SimpleSimon simpleSimon)
            throws Exception {
        profileRole = simpleSimon.profileRole;

        HomeBlade homeBlade;
        JournalBlade journalBlade;
        NodeBlade nodeBlade;
        PostBlade postBlade;
        SecondaryKeysBlade secondaryKeysBlade;
        NodesBlade nodesBlade;
        InvLinksBlade invLinksBlade;

        homeBlade = new HomeBlade(simpleSimon);
        postBlade = new PostBlade(simpleSimon);
        nodeBlade = new NodeBlade(simpleSimon);
        journalBlade = new JournalBlade(simpleSimon);
        secondaryKeysBlade = new SecondaryKeysBlade(simpleSimon);
        nodesBlade = new NodesBlade(simpleSimon);
        invLinksBlade = new InvLinksBlade(simpleSimon);

        requests.put("home", homeBlade);
        requests.put("post", postBlade);
        requests.put("node", nodeBlade);
        requests.put("journal", journalBlade);
        requests.put("secondaryKeys", secondaryKeysBlade);
        requests.put("nodes", nodesBlade);
        requests.put("invLinks", invLinksBlade);

        posts.put("post", postBlade);
    }

    @Override
    public String roleName() {
        return "maintenance";
    }

    @Override
    public RequestBlade requestBlade(String page) {
        RequestBlade rb = requests.get(page);
        if (rb == null)
            rb = profileRole.requestBlade(page);
        return rb;
    }

    @Override
    public PostRequestBlade postRequestBlade(String page) {
        PostRequestBlade rb = posts.get(page);
        if (rb == null)
            rb = profileRole.postRequestBlade(page);
        return rb;
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return homeBlade;
    }

    @Override
    public String getDefaultRequestPage() {
        return "home";
    }

    @Override
    public String menu(HttpServletRequest request,
                       String page,
                       String setTimestamp,
                       String timestamp,
                       String setRole) {
        StringBuffer home = new StringBuffer();
        home.append("<a>Maintenance &#9660;</a>\n");
        home.append("<ul class=\"sub-menu\">\n");

        home.append("<li>\n");
        if ("home".equals(page)) {
            home.append("<a>\n");
        } else {
            home.append("<a href=\"?from=");
            home.append(page);
            home.append("&to=home");
            home.append(setTimestamp);
            home.append(setRole);
            home.append("#rupa\">\n");
        }
        home.append("Home\n");
        home.append("</a>\n");
        home.append("</li>\n");

        home.append("<li>\n");
        if ("journal".equals(page)) {
            home.append("<a>\n");
        } else {
            home.append("<a href=\"?from=");
            home.append(page);
            home.append("&to=journal");
            home.append(setTimestamp);
            home.append(setRole);
            home.append("#rupa\">\n");
        }
        home.append("Journal\n");
        home.append("</a>\n");
        home.append("</li>\n");

        home.append("<li>\n");
        home.append("<a>Secondary Keys:</a>\n");
        home.append("<ul>\n");

        home.append("<li>\n");
        home.append("<a href=\"?from=");
        home.append(page);
        home.append("&to=secondaryKeys&secondaryType=subject&keyPrefix=$v");
        home.append(setTimestamp);
        home.append(setRole);
        home.append("#rupa\">\n");
        home.append("Subjects\n");
        home.append("</a>\n");
        home.append("</li>\n");

        home.append("<li>\n");
        home.append("<a href=\"?from=");
        home.append(page);
        home.append("&to=secondaryKeys&secondaryType=email&keyPrefix=$v");
        home.append(setTimestamp);
        home.append(setRole);
        home.append("#rupa\">\n");
        home.append("Email Addresses\n");
        home.append("</a>\n");
        home.append("</li>\n");

        home.append("<li>\n");
        home.append("<a href=\"?from=");
        home.append(page);
        home.append("&to=secondaryKeys&secondaryType=userType&keyPrefix=$n");
        home.append(setTimestamp);
        home.append(setRole);
        home.append("#rupa\">\n");
        home.append("Roles\n");
        home.append("</a>\n");
        home.append("</li>\n");

        home.append("</ul>\n");
        home.append("</li>\n");

        home.append("<li>\n");
        home.append("<a>User Links:</a>\n");
        home.append("<ul>\n");

        home.append("<li>\n");
        home.append("<a href=\"?from=");
        home.append(page);
        home.append("&to=targets&linkType=users");
        home.append(setTimestamp);
        home.append(setRole);
        home.append("#rupa\">\n");
        home.append("Users\n");
        home.append("</a>\n");
        home.append("</li>\n");

        home.append("<li>\n");
        home.append("<a href=\"?from=");
        home.append(page);
        home.append("&to=sources&linkType=users");
        home.append(setTimestamp);
        home.append(setRole);
        home.append("#rupa\">\n");
        home.append("Owned\n");
        home.append("</a>\n");
        home.append("</li>\n");

        home.append("</ul>\n");
        home.append("</li>\n");

        if (timestamp == null) {
            home.append("<li>\n");
            if ("post".equals(page)) {
                home.append("<a>\n");
            } else {
                home.append("<a href=\"?from=");
                home.append(page);
                home.append("&to=post");
                home.append(setRole);
                home.append("#rupa\">\n");
            }
            home.append("Post\n");
            home.append("</a>\n");
            home.append("</li>\n");
        }
        home.append("</ul>\n");
        return home.toString();
    }
}
