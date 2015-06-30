package org.agilewiki.console.roles;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.Node_NodeInstance;
import org.agilewiki.console.roles.admin.Admin_Role;
import org.agilewiki.console.roles.developer.Developer_Role;
import org.agilewiki.console.roles.system.System_Role;
import org.agilewiki.console.roles.user.User_Role;
import org.agilewiki.console.roles.visitor.Visitor_Role;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Role_Node extends Node_NodeInstance {
    public final static String ID = "$nrole.node";

    public static void create(OODb ooDb) throws Exception {
        ooDb.addTimelessNode(new Role_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
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
