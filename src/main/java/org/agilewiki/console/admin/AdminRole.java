package org.agilewiki.console.admin;

import org.agilewiki.console.PostRequestBlade;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.profile.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * A base role.
 */
public class AdminRole implements Role {

    private Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    private Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();
    private AdminBlade adminBlade;

    public AdminRole(SimpleSimon simpleSimon)
            throws Exception {
        adminBlade = new AdminBlade(simpleSimon);
        requests.put("admin", adminBlade);
    }

    @Override
    public String roleName() {
        return "admin";
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
        return adminBlade;
    }

    @Override
    public String getDefaultRequestPage() {
        return "admin";
    }

    @Override
    public String menu(HttpServletRequest request,
                       String page,
                       String setTimestamp,
                       String timestamp,
                       String setRole) {
        StringBuilder home = new StringBuilder();
        home.append("<a>Admin &#9660;</a>\n");
        home.append("<ul class=\"sub-menu\">\n");

        home.append("<li>");
        if ("admin".equals(page)) {
            home.append("<a>");
        } else {
            home.append("<a href=\"?from=");
            home.append(page);
            home.append("&to=admin");
            home.append(setTimestamp);
            home.append(setRole);
            home.append("#rupa\">");
        }
        home.append("Admin");
        home.append("</a>");
        home.append("</li>\n");

        home.append("</ul>\n");
        return home.toString();
    }
}
