package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class RecreateSystemRole_Node extends JournalEntry_Node {
    private static RecreateSystemRole_Node recreateSystemRole_node;

    public static void create() {
        recreateSystemRole_node = new RecreateSystemRole_Node("$nrecreateSystemRole.node", "$njournalEntry.node");
    }

    public RecreateSystemRole_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new RecreateSystemRole_NodeInstance(nodeId, factoryId);
    }
}
