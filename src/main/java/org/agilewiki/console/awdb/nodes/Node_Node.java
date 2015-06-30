package org.agilewiki.console.awdb.nodes;

import org.agilewiki.console.User_Node;
import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Node_Node extends Metadata_Node {
    public final static String ID = "$nnode.node";

    public static void create(AwDb awDb)
            throws Exception {
        awDb.addTimelessNode(new Node_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
        JournalEntry_Node.create(awDb);
        User_Node.create(awDb);
        Attribute_Node.create(awDb);
    }

    public static void define(String nodeId, String nodeType, String superType, String roleId, String... attributes) {
        AwDb awDb = getOoDb();
        if (nodeType != null) {
            awDb.createSecondaryId(nodeId, Key_Node.NODETYPE_ID, nodeType);
        }
        if (superType != null) {
            awDb.createSecondaryId(nodeId, Key_Node.SUPERTYPE_ID, superType);
        }
        awDb.createLnk1(nodeId,
                Lnk1_Node.OFROLE_ID,
                roleId);
        for (String attributeName : attributes) {
            Attribute_Node.define(attributeName, nodeId);
        }
    }

    public Node_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        throw new UnsupportedOperationException(nodeId);
    }
}
