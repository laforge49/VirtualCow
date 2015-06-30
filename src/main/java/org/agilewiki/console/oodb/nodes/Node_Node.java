package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Node_Node extends Metadata_Node {
    public final static String ID = "$nnode.node";

    public static void create(OODb ooDb)
            throws Exception {
        ooDb.addImmutableNode(new Node_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
        JournalEntry_Node.create(ooDb);
        Role_Node.create(ooDb);
        User_Node.create(ooDb);
        Attribute_Node.create(ooDb);
    }

    public static void define(String nodeId, String nodeType, String superType, String roleId, String... attributes) {
        OODb ooDb = SimpleSimon.simpleSimon.ooDb;
        if (nodeType != null) {
            ooDb.createSecondaryId(nodeId, Key_Node.NODETYPE_ID, nodeType);
        }
        if (superType != null) {
            ooDb.createSecondaryId(nodeId, Key_Node.SUPERTYPE_ID, superType);
        }
        ooDb.createLnk1(nodeId,
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
