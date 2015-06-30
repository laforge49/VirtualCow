package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class JournalEntry_Node extends Node_NodeInstance {
    public final static String ID = "$njournalEntry.node";

    public static void create(OODb ooDb) {
        ooDb.addTimelessNode(new JournalEntry_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public JournalEntry_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new JournalEntry_NodeInstance(nodeId, timestamp);
    }
}
