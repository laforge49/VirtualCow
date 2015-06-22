package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.Node;
import org.agilewiki.utils.ids.composites.Link1Id;

public class JournalEntry_Node extends Node_NodeInstance {
    private static JournalEntry_Node journalEntry_node;
    public final static String ID = "$njournalEntry.node";

    public static JournalEntry_Node get() {
        return journalEntry_node;
    }

    public static void create() {
        journalEntry_node = new JournalEntry_Node(ID, Node_Node.ID);
    }

    public static void define(String nodeId, String ofRole) {
        Node_Node.define(nodeId, Node_Node.ID, ID);
        Link1Id.createLink1(SimpleSimon.simpleSimon.db,
                nodeId,
                Lnk1_Node.OFROLE_ID,
                ofRole);
    }

    public JournalEntry_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new JournalEntry_NodeInstance(nodeId, factoryId);
    }
}
