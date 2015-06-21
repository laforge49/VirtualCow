package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.developer.npje.PostBlade;

/**
 * The developer role.
 */
public class DeveloperRole_Node extends Role_NodeInstance {
    private static DeveloperRole_Node developerRole_node;

    public static DeveloperRole_Node get() {
        return developerRole_node;
    }

    public static void create()
            throws Exception {
        developerRole_node = new DeveloperRole_Node("$ndeveloperRole.node", "$nrole.node");
    }

    private DeveloperBlade developerBlade;
    private FullJournalBlade fullJournalBlade;
    private SubJournalBlade subJournalBlade;
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
    private Lnk1DestinationsBlade targetDestinationsBlade;
    private Lnk1DestinationsBlade originDestinationsBlade;
    private Lnk1DestinationsBlade destinationDestinationsBlade;
    private Lnk1DestinationsBlade ofRoleDestinationsBlade;
    private Lnk1DestinationsBlade userDestinationsBlade;
    private NodesBlade nodesBlade;
    private NodesBlade nodesNodesBlade;
    private NodesBlade keysNodesBlade;
    private NodesBlade lnk1sNodesBlade;
    private InvLinksBlade invLinksBlade;

    public DeveloperRole_Node(String nodeId, String factoryId)
            throws Exception {
        super(nodeId, factoryId);
        niceRoleName = "Developer";
        developerBlade = new DeveloperBlade(this, "developer");
        postBlade = new PostBlade(this, "post");
        nodeBlade = new NodeBlade(this, "node");
        fullJournalBlade = new FullJournalBlade(this, "journal", "Full Journal");
        subJournalBlade = new SubJournalBlade(this, "subJournal", "Journal");

        userOriginsBlade = new Lnk1OriginsBlade(this, "userOrigins", "User Link Origins", "user");
        targetOriginsBlade = new Lnk1OriginsBlade(this, "targetOrigins", "Target Link Origins", "target");
        originOriginsBlade = new Lnk1OriginsBlade(this, "originOrigins", "Origin Link Origins", "origin");
        destinationOriginsBlade = new Lnk1OriginsBlade(this, "destinationOrigins", "Destination Link Origins", "destination");
        ofRoleOriginsBlade = new Lnk1OriginsBlade(this, "ofRoleOrigins", "Of Role Link Origins", "ofRole");

        userDestinationsBlade = new Lnk1DestinationsBlade(this, "userDestinations", "User Link Destinations", "user");
        targetDestinationsBlade = new Lnk1DestinationsBlade(this, "targetDestinations", "Target Link Destinations", "target");
        originDestinationsBlade = new Lnk1DestinationsBlade(this, "originDestinations", "Origin Link Destinations", "origin");
        destinationDestinationsBlade = new Lnk1DestinationsBlade(this, "destinationDestinations", "Destination Link Destinations", "destination");
        ofRoleDestinationsBlade = new Lnk1DestinationsBlade(this, "ofRoleDestinations", "Of Role Link Destinations", "ofRole");

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

        RecreateDeveloperRole_Node.create();
        simpleSimon.db.registerTransaction(RecreateDeveloperRole_NodeInstance.NAME, RecreateDeveloperRole_NodeInstance.class);
        RecreateDeveloperRole_NodeInstance.developerRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateDeveloperRole_NodeInstance.NAME;
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

        menuItem(home, currentPage, setTimestamp, setRole, nodesNodesBlade);
        menuItem(home, currentPage, setTimestamp, setRole, keysNodesBlade);
        menuItem(home, currentPage, setTimestamp, setRole, lnk1sNodesBlade);

        menuItem(home, currentPage, setTimestamp, setRole, postBlade, timestamp != null);
    }
}
