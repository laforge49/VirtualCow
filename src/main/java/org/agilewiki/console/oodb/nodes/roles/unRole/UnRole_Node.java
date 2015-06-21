package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;

public class UnRole_Node extends Role_NodeInstance {
    private static UnRole_Node unRole_node;

    public static UnRole_Node get() {
        return unRole_node;
    }

    public static void create() {
        unRole_node = new UnRole_Node("$nunRole.node", "$nrole.node");
    }

    public UnRole_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
