package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;

public class SystemRole_Node extends Role_NodeInstance {
    private static SystemRole_Node systemRole_node;

    public static SystemRole_Node get() {
        return systemRole_node;
    }

    public static void create() {
        systemRole_node = new SystemRole_Node("$nsystemRole.node", "$nrole.node");
    }

    public SystemRole_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
