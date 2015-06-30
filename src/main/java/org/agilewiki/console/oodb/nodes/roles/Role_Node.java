package org.agilewiki.console.oodb.nodes.roles;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.Node_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.admin.Admin_Role;
import org.agilewiki.console.oodb.nodes.roles.developer.Developer_Role;
import org.agilewiki.console.oodb.nodes.roles.system.System_Role;
import org.agilewiki.console.oodb.nodes.roles.user.User_Role;
import org.agilewiki.console.oodb.nodes.roles.visitor.Visitor_Role;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Role_Node extends Node_NodeInstance {
    public final static String ID = "$nrole.node";

    public static void create(OODb ooDb) throws Exception {
        ooDb.addImmutableNode(new Role_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
        Admin_Role.create(ooDb);
        Developer_Role.create(ooDb);
        System_Role.create(ooDb);
        Visitor_Role.create(ooDb);
        User_Role.create(ooDb);
    }

    public Role_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Role_NodeInstance(nodeId, timestamp);
    }
}
