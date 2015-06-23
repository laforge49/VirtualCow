package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;

public class Metadata_Node extends Node_NodeInstance {
    private static Metadata_Node metaData_Node;
    public final static String ID = "$nmetadata.node";

    public static Metadata_Node get() {
        return metaData_Node;
    }

    public static void create()
            throws Exception {
        metaData_Node = new Metadata_Node(ID);
        Node_Node.create();
        Lnk1_Node.create();
        Key_Node.create();
    }

    public Metadata_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        throw new UnsupportedOperationException();
    }
}
