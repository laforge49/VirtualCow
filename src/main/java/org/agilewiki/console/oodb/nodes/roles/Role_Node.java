package org.agilewiki.console.oodb.nodes.roles;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.Node_Node;
import org.agilewiki.console.oodb.nodes.Node_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.admin.AdminRole_Node;
import org.agilewiki.console.oodb.nodes.roles.developer.DeveloperRole_Node;
import org.agilewiki.console.oodb.nodes.roles.system.SystemRole_Node;
import org.agilewiki.console.oodb.nodes.roles.unRole.UnRole_Node;
import org.agilewiki.console.oodb.nodes.roles.user.UserRole_Node;

public class Role_Node extends Node_NodeInstance {
    private static Role_Node role_node;
    public final static String ID = "$nrole.node";

    public static Role_Node get() {
        return role_node;
    }

    public static void create() throws Exception {
        role_node = new Role_Node(ID, Node_Node.ID);
        AdminRole_Node.create();
        DeveloperRole_Node.create();
        SystemRole_Node.create();
        UnRole_Node.create();
        UserRole_Node.create();
    }

    public Role_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Role_NodeInstance(nodeId, factoryId);
    }
}
