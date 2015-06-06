package org.agilewiki.console.developer;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.RoleBase;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.utils.ids.NameId;

/**
 * The developer role.
 */
public class DeveloperRole extends RoleBase {
    private DeveloperBlade developerBlade;
    private FullJournalBlade fullJournalBlade;
    private SubJournalBlade subJournalBlade;
    private JournalBlade subjectJournalBlade;
    private JournalBlade emailJournalBlade;
    private JournalBlade roleJournalBlade;
    private JournalBlade userJournalBlade;
    private NodeBlade nodeBlade;
    private NodeBlade rolesNodeBlade;
    private PostBlade postBlade;
    private SecondaryKeysBlade subjectsBlade;
    private SecondaryKeysBlade emailAddressesBlade;
    private SecondaryKeysBlade rolesBlade;
    private SecondaryKeysBlade transactionsBlade;
    private NodesBlade nodesBlade;
    private InvLinksBlade invLinksBlade;

    public DeveloperRole(SimpleSimon simpleSimon)
            throws Exception {
        super(simpleSimon);
        developerBlade = new DeveloperBlade(this, "developer");
        requests.put(developerBlade.page, developerBlade);

        postBlade = new PostBlade(this, "post");
        requests.put(postBlade.page, postBlade);
        posts.put(postBlade.page, postBlade);

        nodeBlade = new NodeBlade(this, "node", "Node", null);
        requests.put(nodeBlade.page, nodeBlade);

        rolesNodeBlade = new NodeBlade(this, "rolesNode", "ROLES Node", "$nROLES");
        requests.put(rolesNodeBlade.page, rolesNodeBlade);

        fullJournalBlade = new FullJournalBlade(this, "journal", "Full Journal");
        requests.put(fullJournalBlade.page, fullJournalBlade);

        subJournalBlade = new SubJournalBlade(this, "subJournal", "Journal");
        requests.put(subJournalBlade.page, subJournalBlade);

        subjectJournalBlade = new JournalBlade(this, "subjectJournal",
                "Subject Key Journal",
                NameId.generate("subject"));
        requests.put(subJournalBlade.page, subjectJournalBlade);

        emailJournalBlade = new JournalBlade(this, "emailJournal",
                "Email Address Key Journal",
                NameId.generate("email"));
        requests.put(emailJournalBlade.page, emailJournalBlade);

        roleJournalBlade = new JournalBlade(this, "roleJournal",
                "Role Key Journal",
                NameId.generate("role"));
        requests.put(roleJournalBlade.page, roleJournalBlade);

        userJournalBlade = new JournalBlade(this, "userLinkJournal",
                "User Link Journal",
                NameId.generate("user"));
        requests.put(userJournalBlade.page, userJournalBlade);

        subjectsBlade = new SecondaryKeysBlade(this, "subjects", "Subjects", "subject", "$v");
        requests.put(subjectJournalBlade.page, subjectsBlade);

        emailAddressesBlade = new SecondaryKeysBlade(this, "emailAddresses", "Email Addresses", "email", "$v");
        requests.put(emailAddressesBlade.page, emailAddressesBlade);

        rolesBlade = new SecondaryKeysBlade(this, "roles", "Roles", "role", "$n");
        requests.put(rolesBlade.page, rolesBlade);

        transactionsBlade = new SecondaryKeysBlade(this, "transactions", "Transactions", "transactionName", "$n");
        requests.put(transactionsBlade.page, transactionsBlade);

        nodesBlade = new NodesBlade(this, "nodes");
        requests.put(nodesBlade.page, nodesBlade);

        invLinksBlade = new InvLinksBlade(this, "invLinks");
        requests.put(invLinksBlade.page, invLinksBlade);

        simpleSimon.db.registerTransaction(RecreateDeveloperRoleTransaction.NAME, RecreateDeveloperRoleTransaction.class);
        RecreateDeveloperRoleTransaction.developerRole = this;
    }

    @Override
    public SimpleSimon simpleSimon() {
        return simpleSimon;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateDeveloperRoleTransaction.NAME;
    }

    @Override
    public String roleName() {
        return "developer";
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
        menuItem(home, currentPage, setTimestamp, setRole, developerBlade);
        menuItem(home, currentPage, setTimestamp, setRole, fullJournalBlade);

        home.append("<li>\n");
        home.append("<a>Secondary Keys:</a>\n");
        home.append("<ul>\n");

        menuItem(home, currentPage, setTimestamp, setRole, subjectsBlade);
        menuItem(home, currentPage, setTimestamp, setRole, subjectJournalBlade);
        menuItem(home, currentPage, setTimestamp, setRole, emailAddressesBlade);
        menuItem(home, currentPage, setTimestamp, setRole, emailJournalBlade);
        menuItem(home, currentPage, setTimestamp, setRole, rolesBlade);
        menuItem(home, currentPage, setTimestamp, setRole, roleJournalBlade);
        menuItem(home, currentPage, setTimestamp, setRole, transactionsBlade);

        home.append("</ul>\n");
        home.append("</li>\n");

        home.append("<li>\n");
        home.append("<a>User Links:</a>\n");
        home.append("<ul>\n");

        menuItem(home, currentPage, setTimestamp, setRole, userJournalBlade);

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

        menuItem(home, currentPage, setTimestamp, setRole, rolesNodeBlade);

        menuItem(home, currentPage, setTimestamp, setRole, postBlade, timestamp != null);
    }
}
