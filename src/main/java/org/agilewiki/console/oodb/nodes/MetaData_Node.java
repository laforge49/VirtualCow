package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.key.Key_Node;
import org.agilewiki.console.oodb.nodes.lnk1.Lnk1_Node;

public class Metadata_Node extends Node_NodeInstance {
    private static Metadata_Node metaData_Node;

    public static Metadata_Node get() {
        return metaData_Node;
    }

    public static void create() {
        metaData_Node = new Metadata_Node("$nmetadata.node", "$Nnode.node");
        Node_Node.create();
        Lnk1_Node.create();
        Key_Node.create();
    }

    public Metadata_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        throw new UnsupportedOperationException();
    }
}
