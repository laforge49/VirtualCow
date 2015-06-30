package org.agilewiki.console.roles;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.Node_NodeInstance;
import org.agilewiki.console.roles.admin.Admin_Role;
import org.agilewiki.console.roles.developer.Developer_Role;
import org.agilewiki.console.roles.system.System_Role;
import org.agilewiki.console.roles.user.User_Role;
import org.agilewiki.console.roles.visitor.Visitor_Role;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Role_Node extends Node_NodeInstance {
    public final static String ID = "$nrole.node";

    public static void create(AwDb awDb) throws Exception {
        awDb.addTimelessNode(new Role_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
        Admin_Role.create(awDb);
        Developer_Role.create(awDb);
        System_Role.create(awDb);
        Visitor_Role.create(awDb);
        User_Role.create(awDb);
    }

    public Role_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Role_NodeInstance(nodeId, timestamp);
    }
}
