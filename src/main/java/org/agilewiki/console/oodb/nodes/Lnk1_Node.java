package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;

public class Lnk1_Node extends Metadata_Node {
    private static Lnk1_Node lnk1_node;

    public static Lnk1_Node get() {
        return lnk1_node;
    }

    public static void create() {
        lnk1_node = new Lnk1_Node("$nlnk1.node", "$nnode.node");
    }

    public Lnk1_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        throw new UnsupportedOperationException();
    }
}
