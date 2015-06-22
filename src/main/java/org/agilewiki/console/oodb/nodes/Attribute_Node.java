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
        attribute_node = new Attribute_Node(ID, Node_Node.ID);
    }

    public Attribute_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Attribute_NodeInstance(nodeId, factoryId);
    }

    public void define(String attributeNameId, String nodeId) {
        String attributeId = RandomIds.randomId.generate();
        SecondaryId.createSecondaryId(SimpleSimon.simpleSimon.db,
                attributeId,
                SecondaryId.secondaryId(Key_Node.ATTRIBUTENAME_ID, attributeNameId));
        Link1Id.createLink1(SimpleSimon.simpleSimon.db,
                attributeId, Lnk1_Node.ATTRIBUTEOF_ID,
                nodeId);
    }
}
