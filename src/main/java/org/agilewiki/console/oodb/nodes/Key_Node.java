package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.Node;
import org.agilewiki.utils.ids.composites.Link1Id;

public class Key_Node extends Metadata_Node {
    private static Key_Node key_node;
    public final static String ID = "$nkey.node";

    public static Key_Node get() {
        return key_node;
    }

    public static void create() {
        key_node = new Key_Node(ID, Node_Node.ID);
    }

    public static void define(String nodeId, String targetType) {
        Node_Node.define(nodeId, ID, null);
        Link1Id.createLink1(SimpleSimon.simpleSimon.db,
                nodeId,
                RecreateRole_NodeInstance.TARGET_ID,
                targetType);
    }

    public Key_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Key_NodeInstance(nodeId, factoryId);
    }
}
