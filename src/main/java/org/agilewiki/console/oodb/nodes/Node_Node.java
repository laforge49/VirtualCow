package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;

public class Node_Node extends Metadata_Node {
    private static Node_Node node_node;

    public static Node_Node get() {
        return node_node;
    }

    public static void create() {
        node_node = new Node_Node("$nnode.node", "$nnode.node");
        JournalEntry_Node.create();
        Role_Node.create();
        User_Node.create();
    }

    public Node_Node(String nodeId, String factoryIdb) {
        super(nodeId, factoryIdb);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        throw new UnsupportedOperationException();
    }
}
