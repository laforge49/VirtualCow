package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Metadata_Node extends Node_NodeInstance {
    public final static String ID = "$nmetadata.node";

    public static void create(OODb ooDb)
            throws Exception {
        ooDb.addImmutableNode(new Metadata_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
        Node_Node.create(ooDb);
        Lnk1_Node.create(ooDb);
        Key_Node.create(ooDb);
    }

    public Metadata_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        throw new UnsupportedOperationException();
    }
}
