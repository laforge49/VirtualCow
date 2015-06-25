package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;

public class Node_Node extends Metadata_Node {
    private static Node_Node node_node;
    public final static String ID = "$nnode.node";

    public static Node_Node get() {
        return node_node;
    }

    public static void create()
            throws Exception {
        node_node = new Node_Node(ID);
        JournalEntry_Node.create();
        Role_Node.create();
        User_Node.create();
        Attribute_Node.create();
    }

    public static void define(String nodeId, String nodeType, String superType, String roleId, String... attributes) {
        OODb ooDb = SimpleSimon.simpleSimon.ooDb;
        if (nodeType != null) {
            ooDb.createSecondaryId(nodeId,
                    SecondaryId.secondaryId(Key_Node.NODETYPE_ID, nodeType));
        }
        if (superType != null) {
            ooDb.createSecondaryId(nodeId,
                    SecondaryId.secondaryId(Key_Node.SUPERTYPE_ID, superType));
        }
        Link1Id.createLink1(SimpleSimon.simpleSimon.db,
                nodeId,
                Lnk1_Node.OFROLE_ID,
                roleId);
        for (String attributeName : attributes) {
            Attribute_Node.define(attributeName, nodeId);
        }
    }

    public Node_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        throw new UnsupportedOperationException(nodeId);
    }
}
