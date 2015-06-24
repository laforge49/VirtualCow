package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.RandomIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.Node;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;

public class Attribute_Node extends Node_NodeInstance {
    private static Attribute_Node attribute_node;
    public final static String ID = "$nattribute.node";

    public static Attribute_Node get() {
        return attribute_node;
    }

    public static void create() {
        attribute_node = new Attribute_Node(ID);
    }

    public static void define(String attributeNameId, String nodeId) {
        String attributeId = RandomIds.randomId.generate();
        SimpleSimon.simpleSimon.ooDb.set(attributeId, "$nsubject", attributeNameId);
        SecondaryId.createSecondaryId(SimpleSimon.simpleSimon.db,
                attributeId,
                SecondaryId.secondaryId(Key_Node.NODETYPE_ID, ID));
        SecondaryId.createSecondaryId(SimpleSimon.simpleSimon.db,
                attributeId,
                SecondaryId.secondaryId(Key_Node.ATTRIBUTENAME_ID, attributeNameId));
        Link1Id.createLink1(SimpleSimon.simpleSimon.db,
                attributeId, Lnk1_Node.ATTRIBUTEOF_ID,
                nodeId);
    }

    public Attribute_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new Attribute_NodeInstance(nodeId);
    }
}
