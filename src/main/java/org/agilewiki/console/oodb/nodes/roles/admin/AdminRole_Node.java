package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;

public class AdminRole_Node extends Role_NodeInstance {
    private static AdminRole_Node adminRole_node;

    public static AdminRole_Node get() {
        return adminRole_node;
    }

    public static void create() {
        adminRole_node = new AdminRole_Node("$nadminRole.node", "$nrole.node");
    }

    public AdminRole_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
