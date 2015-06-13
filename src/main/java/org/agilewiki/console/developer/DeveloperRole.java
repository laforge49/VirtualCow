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
    private JournalBlade userLnk1JournalBlade;
    private JournalBlade targetLnk1JournalBlade;
    private JournalBlade originLnk1JournalBlade;
    private JournalBlade destinationLnk1JournalBlade;
    private JournalBlade ofRoleLnk1JournalBlade;
    private NodeBlade nodeBlade;
    private PostBlade postBlade;
    private SecondaryKeysBlade subjectsBlade;
    private SecondaryKeysBlade emailAddressesBlade;
    private SecondaryKeysBlade rolesBlade;
    private SecondaryKeysBlade nodeTypeBlade;
    private SecondaryKeysBlade superTypeBlade;
    private SecondaryKeysBlade invDependentBlade;
    private Lnk1OriginsBlade targetOriginsBlade;
    private Lnk1OriginsBlade originOriginsBlade;
    private Lnk1OriginsBlade destinationOriginsBlade;
    private Lnk1OriginsBlade ofRoleOriginsBlade;
    private Lnk1OriginsBlade userOriginsBlade;
    private NodesBlade nodesBlade;
    private NodesBlade nodesNodesBlade;
    private NodesBlade keysNodesBlade;
    private NodesBlade lnk1sNodesBlade;
    private InvLinksBlade invLinksBlade;

    public DeveloperRole(SimpleSimon simpleSimon, String roleName, String niceRoleName)
            throws Exception {
        super(simpleSimon, roleName, niceRoleName);

        developerBlade = new DeveloperBlade(this, "developer");
        postBlade = new PostBlade(this, "post");
        nodeBlade = new NodeBlade(this, "node", "Node", null);
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

        userOriginsBlade = new Lnk1OriginsBlade(this, "userOrigins", "User Link Origins", "user");
        userLnk1JournalBlade = new JournalBlade(this, "userLnk1Journal",
                "User Link Journal",
                NameId.generate("user"));
        targetOriginsBlade = new Lnk1OriginsBlade(this, "targetOrigins", "Target Link Origins", "target");
        targetLnk1JournalBlade = new JournalBlade(this, "targetLnk1Journal",
                "Target Link Journal",
                NameId.generate("target"));
        originOriginsBlade = new Lnk1OriginsBlade(this, "originOrigins", "Origin Link Origins", "origin");
        originLnk1JournalBlade = new JournalBlade(this, "originLnk1Journal",
                "Origin Link Journal",
                NameId.generate("origin"));
        destinationOriginsBlade = new Lnk1OriginsBlade(this, "destinationOrigins", "Destination Link Origins", "destination");
        destinationLnk1JournalBlade = new JournalBlade(this, "destinationLnk1Journal",
                "Destination Link Journal",
                NameId.generate("destination"));
        ofRoleOriginsBlade = new Lnk1OriginsBlade(this, "ofRoleOrigins", "Of Role Link Origins", "ofRole");
        ofRoleLnk1JournalBlade = new JournalBlade(this, "ofRoleLnk1Journal",
                "Of Role Link Journal",
                NameId.generate("ofRole"));

        subjectsBlade = new SecondaryKeysBlade(this, "subjectValues", "Subjects", "subject");
        emailAddressesBlade = new SecondaryKeysBlade(this, "emailValues", "Email Addresses", "email");
        rolesBlade = new SecondaryKeysBlade(this, "roleValues", "Assigned Roles", "role");
        nodeTypeBlade = new SecondaryKeysBlade(this, "nodeTypeValues", "Node Types", "nodeType");
        superTypeBlade = new SecondaryKeysBlade(this, "superTypeValues", "Super Types", "superType");
        invDependentBlade = new SecondaryKeysBlade(this, "invDependencyValues", "Inverse Dependent Links", "invDependency");

        nodesBlade = new NodesBlade(this, "nodes");
        nodesNodesBlade = new NodesBlade(this, "nodesNodes", "NodeTypes", "$D$nsuperType$nnode.node");
        keysNodesBlade = new NodesBlade(this, "keysNodes", "Key Types", "$D$nsuperType$nkey.node");
        lnk1sNodesBlade = new NodesBlade(this, "lnk1Nodes", "Label Types", "$D$nsuperType$nlnk1.node");
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

        menuItem(home, currentPage, setTimestamp, setRole, subjectJournalBlade);
        menuItem(home, currentPage, setTimestamp, setRole, emailJournalBlade);
        menuItem(home, currentPage, setTimestamp, setRole, roleJournalBlade);

        home.append("</ul>\n");
        home.append("</li>\n");

        home.append("<li>\n");
        home.append("<a>Links:</a>\n");
        home.append("<ul>\n");

        menuItem(home, currentPage, setTimestamp, setRole, userLnk1JournalBlade);
        menuItem(home, currentPage, setTimestamp, setRole, targetLnk1JournalBlade);
        menuItem(home, currentPage, setTimestamp, setRole, originLnk1JournalBlade);
        menuItem(home, currentPage, setTimestamp, setRole, destinationLnk1JournalBlade);
        menuItem(home, currentPage, setTimestamp, setRole, ofRoleLnk1JournalBlade);

        home.append("</ul>\n");
        home.append("</li>\n");

        menuItem(home, currentPage, setTimestamp, setRole, nodesNodesBlade);
        menuItem(home, currentPage, setTimestamp, setRole, keysNodesBlade);
        menuItem(home, currentPage, setTimestamp, setRole, lnk1sNodesBlade);

        menuItem(home, currentPage, setTimestamp, setRole, postBlade, timestamp != null);
    }
}
