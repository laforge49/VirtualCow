package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Metadata_Node extends Node_NodeInstance {
    private static Metadata_Node metaData_Node;
    public final static String ID = "$nmetadata.node";

    public static Metadata_Node get() {
        return metaData_Node;
    }

    public static void create()
            throws Exception {
        metaData_Node = new Metadata_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
        Node_Node.create();
        Lnk1_Node.create();
        Key_Node.create();
    }

    public Metadata_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        throw new UnsupportedOperationException();
    }
}
