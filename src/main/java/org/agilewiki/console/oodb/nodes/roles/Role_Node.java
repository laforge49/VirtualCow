package org.agilewiki.console.oodb.nodes.roles;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.Node_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.admin.Admin_Role;
import org.agilewiki.console.oodb.nodes.roles.developer.Developer_Role;
import org.agilewiki.console.oodb.nodes.roles.system.System_Role;
import org.agilewiki.console.oodb.nodes.roles.user.User_Role;
import org.agilewiki.console.oodb.nodes.roles.visitor.Visitor_Role;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Role_Node extends Node_NodeInstance {
    private static Role_Node role_node;
    public final static String ID = "$nrole.node";

    public static Role_Node get() {
        return role_node;
    }

    public static void create() throws Exception {
        role_node = new Role_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
        Admin_Role.create();
        Developer_Role.create();
        System_Role.create();
        Visitor_Role.create();
        User_Role.create();
    }

    public Role_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Role_NodeInstance(nodeId, timestamp);
    }
}
