package org.agilewiki.vcow.roles;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.Node_Node;
import org.agilewiki.vcow.roles.admin.Admin_Role;
import org.agilewiki.vcow.roles.developer.Developer_Role;
import org.agilewiki.vcow.roles.system.System_Role;
import org.agilewiki.vcow.roles.user.User_Role;
import org.agilewiki.vcow.roles.visitor.Visitor_Role;

public class Role_NodeFactory extends Node_Node {
    public final static String ID = "$nrole.node";

    public static void create(AwDb awDb) throws Exception {
        awDb.addTimelessNode(new Role_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
        Admin_Role.create(awDb);
        Developer_Role.create(awDb);
        System_Role.create(awDb);
        Visitor_Role.create(awDb);
        User_Role.create(awDb);
    }

    public Role_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node instantiateNode(String nodeId, long timestamp) {
        return new Role_NodeInstance(nodeId, timestamp);
    }
}
