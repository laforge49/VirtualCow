package org.agilewiki.console.developer;

import org.agilewiki.console.RecreateRoleTransaction;
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
    private JournalBlade userLnk1JournalBlade;
    private JournalBlade targetLnk1JournalBlade;
    private JournalBlade originLnk1JournalBlade;
    private JournalBlade destinationLnk1JournalBlade;
    private JournalBlade ofRoleLnk1JournalBlade;
    private NodeBlade nodeBlade;
    private NodeBlade rolesNodeBlade;
    private PostBlade postBlade;
    private SecondaryKeysBlade subjectsBlade;
    private SecondaryKeysBlade emailAddressesBlade;
    private SecondaryKeysBlade rolesBlade;
    private SecondaryKeysBlade metaNodesBlade;
    private SecondaryKeysBlade invDependentBlade;
    private Lnk1OriginsBlade targetOriginsBlade;
    private Lnk1OriginsBlade originOriginsBlade;
    private Lnk1OriginsBlade destinationOriginsBlade;
    private Lnk1OriginsBlade ofRoleOriginsBlade;
    private Lnk1OriginsBlade userOriginsBlade;
    private NodesBlade nodesBlade;
    private InvLinksBlade invLinksBlade;

    public DeveloperRole(SimpleSimon simpleSimon, String roleName, String niceRoleName)
            throws Exception {
        super(simpleSimon, roleName, niceRoleName);

        developerBlade = new DeveloperBlade(this, "developer");
        postBlade = new PostBlade(this, "post");
        nodeBlade = new NodeBlade(this, "node", "Node", null);
        rolesNodeBlade = new NodeBlade(this, "metadataNode", "Metadata Node", "$nmetadata.node");
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

        userOriginsBlade = new Lnk1OriginsBlade(this, "userOrigins", "User Link Origins", "user", "$n");
        userLnk1JournalBlade = new JournalBlade(this, "userLnk1Journal",
                "User Link Journal",
                NameId.generate("user"));
        targetOriginsBlade = new Lnk1OriginsBlade(this, "targetOrigins", "Target Link Origins", "target", "$n");
        targetLnk1JournalBlade = new JournalBlade(this, "targetLnk1Journal",
                "Target Link Journal",
                NameId.generate("target"));
        originOriginsBlade = new Lnk1OriginsBlade(this, "originOrigins", "Origin Link Origins", "origin", "$n");
        originLnk1JournalBlade = new JournalBlade(this, "originLnk1Journal",
                "Origin Link Journal",
                NameId.generate("origin"));
        destinationOriginsBlade = new Lnk1OriginsBlade(this, "destinationOrigins", "Destination Link Origins", "destination", "$n");
        destinationLnk1JournalBlade = new JournalBlade(this, "destinationLnk1Journal",
                "Destination Link Journal",
                NameId.generate("destination"));
        ofRoleOriginsBlade = new Lnk1OriginsBlade(this, "ofRoleOrigins", "Of Role Link Origins", "ofRole", "$n");
        ofRoleLnk1JournalBlade = new JournalBlade(this, "ofRoleLnk1Journal",
                "Of Role Link Journal",
                NameId.generate("ofRole"));

        subjectsBlade = new SecondaryKeysBlade(this, "subjects", "Subjects", "subject", "$v");
        emailAddressesBlade = new SecondaryKeysBlade(this, "emailAddresses", "Email Addresses", "email", "$v");
        rolesBlade = new SecondaryKeysBlade(this, "roles", "Assigned Roles", "role", "$n");
        metaNodesBlade = new SecondaryKeysBlade(this, "mnodes", "Node Types", "nodeType", "$n");
        invDependentBlade = new SecondaryKeysBlade(this, "invDependents", "Inverse Dependent Links", "invDependency", "$n");

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
        menuItem(home, currentPage, setTimestamp, setRole, metaNodesBlade);
        menuItem(home, currentPage, setTimestamp, setRole, invDependentBlade);

        home.append("</ul>\n");
        home.append("</li>\n");

        home.append("<li>\n");
        home.append("<a>Links:</a>\n");
        home.append("<ul>\n");

        menuItem(home, currentPage, setTimestamp, setRole, userOriginsBlade);
        menuItem(home, currentPage, setTimestamp, setRole, userLnk1JournalBlade);
        menuItem(home, currentPage, setTimestamp, setRole, targetOriginsBlade);
        menuItem(home, currentPage, setTimestamp, setRole, targetLnk1JournalBlade);
        menuItem(home, currentPage, setTimestamp, setRole, originOriginsBlade);
        menuItem(home, currentPage, setTimestamp, setRole, originLnk1JournalBlade);
        menuItem(home, currentPage, setTimestamp, setRole, destinationOriginsBlade);
        menuItem(home, currentPage, setTimestamp, setRole, destinationLnk1JournalBlade);
        menuItem(home, currentPage, setTimestamp, setRole, ofRoleOriginsBlade);
        menuItem(home, currentPage, setTimestamp, setRole, ofRoleLnk1JournalBlade);

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
