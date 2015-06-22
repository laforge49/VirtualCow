package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;
import org.agilewiki.utils.ids.composites.SecondaryId;

public class Node_Node extends Metadata_Node {
    private static Node_Node node_node;
    public final static String ID = "$nnode.node";

    public static Node_Node get() {
        return node_node;
    }

    public static void create()
            throws Exception {
        node_node = new Node_Node(ID, ID);
        JournalEntry_Node.create();
        Role_Node.create();
        User_Node.create();
    }

    public static void define(String nodeId, String nodeType, String superType) {
        if (nodeType != null) {
            SecondaryId.createSecondaryId(SimpleSimon.simpleSimon.db,
                    nodeId,
                    SecondaryId.secondaryId(Key_Node.NODETYPE_ID, nodeType));
        }
        if (superType != null) {
            SecondaryId.createSecondaryId(SimpleSimon.simpleSimon.db,
                    nodeId,
                    SecondaryId.secondaryId(Key_Node.SUPERTYPE_ID, superType));
        }
    }

    public Node_Node(String nodeId, String factoryIdb) {
        super(nodeId, factoryIdb);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        throw new UnsupportedOperationException();
    }
}
