package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;

public class DeveloperRole_Node extends Role_NodeInstance {
    private static DeveloperRole_Node developerRole_node;

    public static void create() {
        developerRole_node = new DeveloperRole_Node("$ndeveloperRole.node", "$nrole.node");
    }

    public DeveloperRole_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
