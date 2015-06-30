package org.agilewiki.console.awdb.nodes;

import org.agilewiki.console.RandomIds;
import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Attribute_Node extends Node_NodeInstance {
    public final static String ID = "$nattribute.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new Attribute_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public static void define(String attributeNameId, String nodeId) {
        AwDb awDb = getOoDb();
        String attributeId = RandomIds.randomId.generate();
        awDb.set(attributeId, "$nsubject", attributeNameId + " - " + nodeId);
        awDb.createSecondaryId(attributeId, Key_Node.NODETYPE_ID, ID);
        awDb.createSecondaryId(attributeId, Key_Node.ATTRIBUTENAME_ID, attributeNameId);
        awDb.createLnk1(attributeId, Lnk1_Node.ATTRIBUTEOF_ID, nodeId);
    }

    public Attribute_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Attribute_NodeInstance(nodeId, timestamp);
    }
}
