package org.agilewiki.console.oodb.nodes.roles.user;

import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;

public class UserRole_Node extends Role_NodeInstance {
    private static UserRole_Node userRole_node;

    public static UserRole_Node get() {
        return userRole_node;
    }

    public static void create() {
        userRole_node = new UserRole_Node("$nuserRole.node", "$nrole.node");
    }

    public UserRole_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
