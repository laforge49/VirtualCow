package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;

public class Key_Node extends Metadata_Node {
    private static Key_Node key_node;

    public static Key_Node get() {
        return key_node;
    }

    public static void create() {
        key_node = new Key_Node("$nkey.node", "$nnode.node");
    }

    public Key_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Key_NodeInstance(nodeId, factoryId);
    }
}
