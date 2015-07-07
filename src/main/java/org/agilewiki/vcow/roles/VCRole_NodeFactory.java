package org.agilewiki.vcow.roles;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.Node_Node;
import org.agilewiki.awdb.nodes.Role_NodeFactory;
import org.agilewiki.vcow.roles.admin.Admin_Role;
import org.agilewiki.vcow.roles.developer.Developer_Role;
import org.agilewiki.vcow.roles.system.System_Role;
import org.agilewiki.vcow.roles.user.User_Role;
import org.agilewiki.vcow.roles.visitor.Visitor_Role;

public class VCRole_NodeFactory extends Role_NodeFactory {

    public static void create(AwDb awDb) throws Exception {
        awDb.addTimelessNode(new VCRole_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
        Admin_Role.create(awDb);
        Developer_Role.create(awDb);
        System_Role.create(awDb);
        Visitor_Role.create(awDb);
        User_Role.create(awDb);
    }

    public VCRole_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node instantiateNode(String nodeId, long timestamp) {
        return new VCRole_Node(nodeId, timestamp);
    }
}
