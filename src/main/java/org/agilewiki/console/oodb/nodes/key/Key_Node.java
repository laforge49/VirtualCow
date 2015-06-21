package org.agilewiki.console.oodb.nodes.key;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.Metadata_Node;

public class Key_Node extends Metadata_Node {
    private static Key_Node key_node;

    public static void create() {
        key_node = new Key_Node("$nkey.node", "$nnode.node");
        Email_Key.create();
        InvDependency_Key.create();
        NodeType_Key.create();
        Role_Key.create();
        Subject_Key.create();
        SuperType_Key.create();
    }

    public Key_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Key_NodeInstance(nodeId, factoryId);
    }
}
