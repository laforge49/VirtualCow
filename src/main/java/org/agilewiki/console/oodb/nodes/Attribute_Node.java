package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.RandomIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Attribute_Node extends Node_NodeInstance {
    private static Attribute_Node attribute_node;
    public final static String ID = "$nattribute.node";

    public static Attribute_Node get() {
        return attribute_node;
    }

    public static void create() {
        attribute_node = new Attribute_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public static void define(String attributeNameId, String nodeId) {
        OODb ooDb = SimpleSimon.simpleSimon.ooDb;
        String attributeId = RandomIds.randomId.generate();
        ooDb.set(attributeId, "$nsubject", attributeNameId + " - " + nodeId);
        ooDb.createSecondaryId(attributeId, Key_Node.NODETYPE_ID, ID);
        ooDb.createSecondaryId(attributeId, Key_Node.ATTRIBUTENAME_ID, attributeNameId);
        ooDb.createLnk1(attributeId, Lnk1_Node.ATTRIBUTEOF_ID, nodeId);
    }

    public Attribute_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Attribute_NodeInstance(nodeId, timestamp);
    }
}
