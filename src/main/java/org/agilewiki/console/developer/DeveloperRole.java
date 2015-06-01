package org.agilewiki.console.developer;

import org.agilewiki.console.*;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.Journal;

import java.util.HashMap;
import java.util.Map;

/**
 * The developer role.
 */
public class DeveloperRole implements Role {
    private Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    private Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();
    private DeveloperBlade developerBlade;
    private FullJournalBlade fullJournalBlade;
    private SubJournalBlade subJournalBlade;
    private JournalBlade subjectJournalBlade;
    private JournalBlade emailJournalBlade;
    private JournalBlade roleJournalBlade;
    private JournalBlade userJournalBlade;
    private NodeBlade nodeBlade;
    private PostBlade postBlade;
    private SecondaryKeysBlade subjectsBlade;
    private SecondaryKeysBlade emailAddressesBlade;
    private SecondaryKeysBlade rolesBlade;
    private SecondaryKeysBlade transactionsBlade;
    private NodesBlade nodesBlade;
    private InvLinksBlade invLinksBlade;

    public DeveloperRole(SimpleSimon simpleSimon)
            throws Exception {
        developerBlade = new DeveloperBlade(simpleSimon);
        postBlade = new PostBlade(simpleSimon);
        nodeBlade = new NodeBlade(simpleSimon);
        fullJournalBlade = new FullJournalBlade(simpleSimon, "Full Journal");
        subJournalBlade = new SubJournalBlade(simpleSimon, "Journal");
        subjectJournalBlade = new JournalBlade(simpleSimon,
                "Subject Key Journal",
                NameId.generate("subject"));
        emailJournalBlade = new JournalBlade(simpleSimon,
                "Email Address Key Journal",
                NameId.generate("email"));
        roleJournalBlade = new JournalBlade(simpleSimon,
                "Role Key Journal",
                NameId.generate("role"));
        userJournalBlade = new JournalBlade(simpleSimon,
                "User Link Journal",
                NameId.generate("user"));
        subjectsBlade = new SecondaryKeysBlade(simpleSimon, "Subjects", "subject", "$v");
        emailAddressesBlade = new SecondaryKeysBlade(simpleSimon, "Email Addresses", "email", "$v");
        rolesBlade = new SecondaryKeysBlade(simpleSimon, "Roles", "role", "$n");
        transactionsBlade = new SecondaryKeysBlade(simpleSimon, "Transactions", "transactionName", "$n");
        nodesBlade = new NodesBlade(simpleSimon);
        invLinksBlade = new InvLinksBlade(simpleSimon);

        requests.put("developer", developerBlade);
        requests.put("post", postBlade);
        requests.put("node", nodeBlade);
        requests.put("journal", fullJournalBlade);
        requests.put("subJournal", subJournalBlade);
        requests.put("subjectJournal", subjectJournalBlade);
        requests.put("emailJournal", emailJournalBlade);
        requests.put("roleJournal", roleJournalBlade);
        requests.put("userLinkJournal", userJournalBlade);
        requests.put("subjects", subjectsBlade);
        requests.put("emailAddresses", emailAddressesBlade);
        requests.put("roles", rolesBlade);
        requests.put("transactions", transactionsBlade);
        requests.put("nodes", nodesBlade);
        requests.put("invLinks", invLinksBlade);

        posts.put("post", postBlade);
    }

    @Override
    public String roleName() {
        return "developer";
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
        return developerBlade;
    }

    @Override
    public String getDefaultRequestPage() {
        return "developer";
    }

    @Override
    public String niceRoleName() {
        return "Developer";
    }

    @Override
    public void menuItems(StringBuilder home,
                          String currentPage,
                          String setTimestamp,
                          String timestamp,
                          String setRole) {
        menuItem(home, currentPage, setTimestamp, setRole, "developer", developerBlade.niceName());
        menuItem(home, currentPage, setTimestamp, setRole, "journal", fullJournalBlade.niceName());

        home.append("<li>\n");
        home.append("<a>Secondary Keys:</a>\n");
        home.append("<ul>\n");

        menuItem(home, currentPage, setTimestamp, setRole, "subjects", subjectsBlade.niceName());
        menuItem(home, currentPage, setTimestamp, setRole, "subjectJournal", subjectJournalBlade.niceName());
        menuItem(home, currentPage, setTimestamp, setRole, "emailAddresses", emailAddressesBlade.niceName());
        menuItem(home, currentPage, setTimestamp, setRole, "emailJournal", emailJournalBlade.niceName());
        menuItem(home, currentPage, setTimestamp, setRole, "roles", rolesBlade.niceName());
        menuItem(home, currentPage, setTimestamp, setRole, "roleJournal", roleJournalBlade.niceName());
        menuItem(home, currentPage, setTimestamp, setRole, "transactions", transactionsBlade.niceName());

        home.append("</ul>\n");
        home.append("</li>\n");

        home.append("<li>\n");
        home.append("<a>User Links:</a>\n");
        home.append("<ul>\n");

        menuItem(home, currentPage, setTimestamp, setRole, "userLinkJournal", userJournalBlade.niceName());

        /*
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
        */

        home.append("</ul>\n");
        home.append("</li>\n");

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
        home.append(postBlade.niceName() + "\n");
        home.append("</a>\n");
        home.append("</li>\n");
    }
}
