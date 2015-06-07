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
    private SecondaryKeysBlade link1sBlade;
    private SecondaryKeysBlade invDependentBlade;
    private NodesBlade nodesBlade;
    private InvLinksBlade invLinksBlade;

    public DeveloperRole(SimpleSimon simpleSimon, String roleName, String niceRoleName)
            throws Exception {
        super(simpleSimon, roleName, niceRoleName);

        developerBlade = new DeveloperBlade(this, "developer");
        postBlade = new PostBlade(this, "post");
        nodeBlade = new NodeBlade(this, "node", "Node", null);
        rolesNodeBlade = new NodeBlade(this, "metadataNode", "Metadata Node", "$nmetadata");
        fullJournalBlade = new FullJournalBlade(this, "journal", "Full Journal");
        subJournalBlade = new SubJournalBlade(this, "subJournal", "Journal");
        subjectJournalBlade = new JournalBlade(this, "subjectJournal",
                "Subject Journal",
                NameId.generate("subject"));
        emailJournalBlade = new JournalBlade(this, "emailJournal",
                "Email Address Journal",
                NameId.generate("email"));
        roleJournalBlade = new JournalBlade(this, "roleJournal",
                "Role Assignment Journal",
                NameId.generate("role"));
        userJournalBlade = new JournalBlade(this, "userLinkJournal",
                "User Link Journal",
                NameId.generate("user"));

        subjectsBlade = new SecondaryKeysBlade(this, "subjects", "Subjects", "subject", "$v");
        emailAddressesBlade = new SecondaryKeysBlade(this, "emailAddresses", "Email Addresses", "email", "$v");
        rolesBlade = new SecondaryKeysBlade(this, "roles", "Assigned Roles", "role", "$n");
        transactionsBlade = new SecondaryKeysBlade(this, "transactions", "Transactions", "transactionName", "$n");
        link1sBlade = new SecondaryKeysBlade(this, "link1s", "Link1 Links", ".link1", "$n");
        invDependentBlade = new SecondaryKeysBlade(this, "invDependents", "Inverse Dependent Links", "invDependent", "$n");

        nodesBlade = new NodesBlade(this, "nodes");
        invLinksBlade = new InvLinksBlade(this, "invLinks");

        simpleSimon.db.registerTransaction(RecreateDeveloperRoleTransaction.NAME, RecreateDeveloperRoleTransaction.class);
        RecreateDeveloperRoleTransaction.developerRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateDeveloperRoleTransaction.NAME;
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return developerBlade;
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
        menuItem(home, currentPage, setTimestamp, setRole, link1sBlade);
        menuItem(home, currentPage, setTimestamp, setRole, invDependentBlade);

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
