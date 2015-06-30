package org.agilewiki.console.awdb.nodes;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Metadata_Node extends Node_NodeInstance {
    public final static String ID = "$nmetadata.node";

    public static void create(AwDb awDb)
            throws Exception {
        awDb.addTimelessNode(new Metadata_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
        Node_Node.create(awDb);
        Lnk1_Node.create(awDb);
        Key_Node.create(awDb);
    }

    public Metadata_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        throw new UnsupportedOperationException();
    }
}
