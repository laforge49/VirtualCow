package org.agilewiki.console.internals;

import org.agilewiki.console.*;

import java.util.HashMap;
import java.util.Map;

/**
 * The internals role.
 */
public class InternalsRole implements Role {
    private Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    private Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();
    private InternalsBlade internalsBlade;

    public InternalsRole(SimpleSimon simpleSimon)
            throws Exception {
        InternalsBlade internalsBlade;
        JournalBlade journalBlade;
        NodeBlade nodeBlade;
        PostBlade postBlade;
        SecondaryKeysBlade secondaryKeysBlade;
        NodesBlade nodesBlade;
        InvLinksBlade invLinksBlade;

        internalsBlade = new InternalsBlade(simpleSimon);
        postBlade = new PostBlade(simpleSimon);
        nodeBlade = new NodeBlade(simpleSimon);
        journalBlade = new JournalBlade(simpleSimon);
        secondaryKeysBlade = new SecondaryKeysBlade(simpleSimon);
        nodesBlade = new NodesBlade(simpleSimon);
        invLinksBlade = new InvLinksBlade(simpleSimon);

        requests.put("internals", internalsBlade);
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
        return "internals";
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
        return internalsBlade;
    }

    @Override
    public String getDefaultRequestPage() {
        return "internals";
    }

    @Override
    public String niceRoleName() {
        return "Internals";
    }

    @Override
    public void menuItems(StringBuilder home,
                          String currentPage,
                          String setTimestamp,
                          String timestamp,
                          String setRole) {
        menuItem(home, currentPage, setTimestamp, setRole, "internals", "Internals");
        menuItem(home, currentPage, setTimestamp, setRole, "journal", "Journal");

        home.append("<li>\n");
        home.append("<a>Secondary Keys:</a>\n");
        home.append("<ul>\n");

        home.append("<li>\n");
        home.append("<a href=\"?from=");
        home.append(currentPage);
        home.append("&to=secondaryKeys&secondaryType=subject&keyPrefix=$v");
        home.append(setTimestamp);
        home.append(setRole);
        home.append("#rupa\">\n");
        home.append("Subjects\n");
        home.append("</a>\n");
        home.append("</li>\n");

        home.append("<li>\n");
        home.append("<a href=\"?from=");
        home.append(currentPage);
        home.append("&to=secondaryKeys&secondaryType=email&keyPrefix=$v");
        home.append(setTimestamp);
        home.append(setRole);
        home.append("#rupa\">\n");
        home.append("Email Addresses\n");
        home.append("</a>\n");
        home.append("</li>\n");

        home.append("<li>\n");
        home.append("<a href=\"?from=");
        home.append(currentPage);
        home.append("&to=secondaryKeys&secondaryType=role&keyPrefix=$n");
        home.append(setTimestamp);
        home.append(setRole);
        home.append("#rupa\">\n");
        home.append("Roles\n");
        home.append("</a>\n");
        home.append("</li>\n");

        home.append("</ul>\n");
        home.append("</li>\n");
        /*

        home.append("<li>\n");
        home.append("<a>User Links:</a>\n");
        home.append("<ul>\n");

        home.append("<li>\n");
        home.append("<a href=\"?from=");
        home.append(currentPage);
        home.append("&to=targets&linkType=users");
        home.append(setTimestamp);
        home.append(setRole);
        home.append("#rupa\">\n");
        home.append("Users\n");
        home.append("</a>\n");
        home.append("</li>\n");

        home.append("<li>\n");
        home.append("<a href=\"?from=");
        home.append(currentPage);
        home.append("&to=sources&linkType=users");
        home.append(setTimestamp);
        home.append(setRole);
        home.append("#rupa\">\n");
        home.append("Owned\n");
        home.append("</a>\n");
        home.append("</li>\n");

        home.append("</ul>\n");
        home.append("</li>\n");
        */

        home.append("<li>\n");
        if ("post".equals(currentPage) || timestamp != null) {
            home.append("<a>\n");
        } else {
            home.append("<a href=\"?from=");
            home.append(currentPage);
            home.append("&to=post");
            home.append(setRole);
            home.append("#rupa\">\n");
        }
        home.append("Post\n");
        home.append("</a>\n");
        home.append("</li>\n");
    }
}
