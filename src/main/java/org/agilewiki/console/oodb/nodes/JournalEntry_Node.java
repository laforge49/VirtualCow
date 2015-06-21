package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;

public class JournalEntry_Node extends Node_NodeInstance {
    private static JournalEntry_Node journalEntry_node;

    public static void create() {
        journalEntry_node = new JournalEntry_Node("$njournalEntry.node", "$nnode.node");
    }

    public JournalEntry_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new JournalEntry_NodeInstance(nodeId, factoryId);
    }
}
