package org.agilewiki.vcow.roles.developer;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.Lnk1_NodeFactory;
import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.roles.VCRole_Node;
import org.agilewiki.vcow.roles.developer.npje.PostBlade;

/**
 * The developer role.
 */
public class Developer_Role extends VCRole_Node {
    public final static String ID = "$ndeveloper.role";

    public static void create(AwDb awDb)
            throws Exception {
        awDb.addTimelessNode(new Developer_Role(ID, FactoryRegistry.MAX_TIMESTAMP));
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
    private SecondaryKeysBlade attributeNameBlade;

    private Lnk1OriginsBlade targetOriginsBlade;
    private Lnk1OriginsBlade originOriginsBlade;
    private Lnk1OriginsBlade destinationOriginsBlade;
    private Lnk1OriginsBlade userOriginsBlade;
    private Lnk1OriginsBlade attributeOfOriginsBlade;
    private Lnk1OriginsBlade userGroupOriginsBlade;
    private Lnk1OriginsBlade memberOfOriginsBlade;
    private Lnk1OriginsBlade userDomainOriginsBlade;
    private Lnk1OriginsBlade roleOfRealmOriginsBlade;
    private Lnk1OriginsBlade groupDomainOriginsBlade;
    private Lnk1OriginsBlade domainForRealmOriginsBlade;
    private Lnk1OriginsBlade domainForRoleOriginsBlade;
    private Lnk1OriginsBlade ofDomainOriginsBlade;

    private Lnk1DestinationsBlade targetDestinationsBlade;
    private Lnk1DestinationsBlade originDestinationsBlade;
    private Lnk1DestinationsBlade destinationDestinationsBlade;
    private Lnk1DestinationsBlade userDestinationsBlade;
    private Lnk1DestinationsBlade attributeOfDestinationsBlade;
    private Lnk1DestinationsBlade userGroupDestinationsBlade;
    private Lnk1DestinationsBlade memberOfDestinationsBlade;
    private Lnk1DestinationsBlade userDomainDestinationsBlade;
    private Lnk1DestinationsBlade roleOfRealmDestinationsBlade;
    private Lnk1DestinationsBlade groupDomainDestinationsBlade;
    private Lnk1DestinationsBlade domainForRealmDestinationsBlade;
    private Lnk1DestinationsBlade domainForRoleDestinationsBlade;
    private Lnk1DestinationsBlade ofDomainDestinationsBlade;

    private NodesBlade nodesBlade;
    private NodesBlade nodesNodesBlade;
    private NodesBlade keysNodesBlade;
    private NodesBlade lnk1sNodesBlade;
    private NodesBlade rolesNodesBlade;
    private InvLinksBlade invLinksBlade;

    public Developer_Role(String nodeId, long timestamp)
            throws Exception {
        super(nodeId, timestamp);
        niceRoleName = "Developer";
        developerBlade = new DeveloperBlade(this, "developer");
        postBlade = new PostBlade(this, "post");
        nodeBlade = new NodeBlade(this, "node");
        fullJournalBlade = new FullJournalBlade(this, "journal", "Full Journal");
        subJournalBlade = new SubJournalBlade(this, "subJournal", "Journal");

        userOriginsBlade = new Lnk1OriginsBlade(this, "userOrigins", "User Link Origins", Lnk1_NodeFactory.USER_ID);
        targetOriginsBlade = new Lnk1OriginsBlade(this, "targetOrigins", "Target Link Origins", Lnk1_NodeFactory.TARGET_ID);
        originOriginsBlade = new Lnk1OriginsBlade(this, "originOrigins", "Origin Link Origins", Lnk1_NodeFactory.ORIGIN_ID);
        destinationOriginsBlade = new Lnk1OriginsBlade(this, "destinationOrigins", "Destination Link Origins", Lnk1_NodeFactory.DESTINATION_ID);
        attributeOfOriginsBlade = new Lnk1OriginsBlade(this, "attributeOfOrigins", "Attribute of Link Origins", Lnk1_NodeFactory.ATTRIBUTEOF_ID);

        userGroupOriginsBlade = new Lnk1OriginsBlade(this, "userGroupOrigins", "Attribute of Link UserGroup", Lnk1_NodeFactory.USER_GROUP_ID);
        memberOfOriginsBlade = new Lnk1OriginsBlade(this, "memberOfOrigins", "Attribute of Link MemberOf", Lnk1_NodeFactory.MEMBER_OF_ID);
        userDomainOriginsBlade = new Lnk1OriginsBlade(this, "userDomainOrigins", "Attribute of Link UserDomain", Lnk1_NodeFactory.USER_DOMAIN_ID);
        roleOfRealmOriginsBlade = new Lnk1OriginsBlade(this, "roleOfRealmOrigins", "Attribute of Link RoleOfRealm", Lnk1_NodeFactory.ROLE_OF_REALM_ID);
        groupDomainOriginsBlade = new Lnk1OriginsBlade(this, "groupDomainOrigins", "Attribute of Link GroupDomain", Lnk1_NodeFactory.GROUP_DOMAIN_ID);
        domainForRealmOriginsBlade = new Lnk1OriginsBlade(this, "domainForRealmOrigins", "Attribute of Link DomainForRealm", Lnk1_NodeFactory.DOMAIN_FOR_REALM_ID);
        domainForRoleOriginsBlade = new Lnk1OriginsBlade(this, "domainForRoleOrigins", "Attribute of Link DomainForRole", Lnk1_NodeFactory.DOMAIN_FOR_ROLE_ID);
        ofDomainOriginsBlade = new Lnk1OriginsBlade(this, "ofDomainOrigins", "Attribute of Link OfDomain", Lnk1_NodeFactory.OF_DOMAIN_ID);

        userDestinationsBlade = new Lnk1DestinationsBlade(this, "userDestinations", "User Link Destinations", Lnk1_NodeFactory.USER_ID);
        targetDestinationsBlade = new Lnk1DestinationsBlade(this, "targetDestinations", "Target Link Destinations", Lnk1_NodeFactory.TARGET_ID);
        originDestinationsBlade = new Lnk1DestinationsBlade(this, "originDestinations", "Origin Link Destinations", Lnk1_NodeFactory.ORIGIN_ID);
        destinationDestinationsBlade = new Lnk1DestinationsBlade(this, "destinationDestinations", "Destination Link Destinations", Lnk1_NodeFactory.DESTINATION_ID);
        attributeOfDestinationsBlade = new Lnk1DestinationsBlade(this, "attributeOfDestinations", "Attribute of Link Destinations", Lnk1_NodeFactory.ATTRIBUTEOF_ID);

        userGroupDestinationsBlade = new Lnk1DestinationsBlade(this, "userGroupDestinations", "Attribute of Link UserGroup", Lnk1_NodeFactory.USER_GROUP_ID);
        memberOfDestinationsBlade = new Lnk1DestinationsBlade(this, "memberOfDestinations", "Attribute of Link MemberOf", Lnk1_NodeFactory.MEMBER_OF_ID);
        userDomainDestinationsBlade = new Lnk1DestinationsBlade(this, "userDomainDestinations", "Attribute of Link UserDomain", Lnk1_NodeFactory.USER_DOMAIN_ID);
        roleOfRealmDestinationsBlade = new Lnk1DestinationsBlade(this, "roleOfRealmDestinations", "Attribute of Link RoleOfRealm", Lnk1_NodeFactory.ROLE_OF_REALM_ID);
        groupDomainDestinationsBlade = new Lnk1DestinationsBlade(this, "groupDomainDestinations", "Attribute of Link GroupDomain", Lnk1_NodeFactory.GROUP_DOMAIN_ID);
        domainForRealmDestinationsBlade = new Lnk1DestinationsBlade(this, "domainForRealmDestinations", "Attribute of Link DomainForRealm", Lnk1_NodeFactory.DOMAIN_FOR_REALM_ID);
        domainForRoleDestinationsBlade = new Lnk1DestinationsBlade(this, "domainForRoleDestinations", "Attribute of Link DomainForRole", Lnk1_NodeFactory.DOMAIN_FOR_ROLE_ID);
        ofDomainDestinationsBlade = new Lnk1DestinationsBlade(this, "ofDomainDestinations", "Attribute of Link OfDomain", Lnk1_NodeFactory.OF_DOMAIN_ID);

        subjectsBlade = new SecondaryKeysBlade(this, "subjectValues", "Subjects", "subject");
        emailAddressesBlade = new SecondaryKeysBlade(this, "emailValues", "Email Addresses", "email");
        rolesBlade = new SecondaryKeysBlade(this, "roleValues", "Assigned Roles", "role");
        nodeTypeBlade = new SecondaryKeysBlade(this, "nodeTypeValues", "Node Types", "nodeType");
        superTypeBlade = new SecondaryKeysBlade(this, "superTypeValues", "Super Types", "superType");
        invDependentBlade = new SecondaryKeysBlade(this, "invDependencyValues", "Inverse Dependent Links", "invDependency");
        attributeNameBlade = new SecondaryKeysBlade(this, "attributeNameValues", "Attribute Names", "attributeName");

        nodesBlade = new NodesBlade(this, "nodes");
        nodesNodesBlade = new NodesBlade(this, "nodesNodes", "NodeTypes", "$D$nnodeType$nnode.node");
        keysNodesBlade = new NodesBlade(this, "keysNodes", "Key Types", "$D$nnodeType$nkey.node");
        lnk1sNodesBlade = new NodesBlade(this, "lnk1Nodes", "Label Types", "$D$nnodeType$nlnk1.node");
        rolesNodesBlade = new NodesBlade(this, "roleNodes", "Role Types", "$D$nnodeType$nrole.node");
        invLinksBlade = new InvLinksBlade(this, "invLinks");
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
        menuItem(home, currentPage, setTimestamp, setRole, rolesNodesBlade);

        menuItem(home, currentPage, setTimestamp, setRole, postBlade, timestamp != null);
    }
}
