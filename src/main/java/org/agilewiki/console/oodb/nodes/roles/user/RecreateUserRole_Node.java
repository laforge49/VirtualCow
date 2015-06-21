package org.agilewiki.console.oodb.nodes.roles.user;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class RecreateUserRole_Node extends JournalEntry_Node {
    private static RecreateUserRole_Node recreateUserRole_node;

    public static void create() {
        recreateUserRole_node = new RecreateUserRole_Node("$nrecreateUserRole.node", "$njournalEntry.node");
    }

    public RecreateUserRole_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new RecreateUserRole_NodeInstance(nodeId, factoryId);
    }
}
