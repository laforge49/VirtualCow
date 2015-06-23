package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;

public class JournalEntry_Node extends Node_NodeInstance {
    private static JournalEntry_Node journalEntry_node;
    public final static String ID = "$njournalEntry.node";

    public static JournalEntry_Node get() {
        return journalEntry_node;
    }

    public static void create() {
        journalEntry_node = new JournalEntry_Node(ID);
    }

    public JournalEntry_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new JournalEntry_NodeInstance(nodeId);
    }
}
