package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.Node;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;

public class Lnk1_Node extends Metadata_Node {
    private static Lnk1_Node lnk1_node;

    public static Lnk1_Node get() {
        return lnk1_node;
    }

    public static void create() {
        lnk1_node = new Lnk1_Node("$nlnk1.node", "$nnode.node");
    }

    public static void define(String nodeId, String invDependency, String originType, String destinationType, String ofRole) {
        Node_Node.define(nodeId, RecreateRole_NodeInstance.LNK1_NODE_ID, null);
        if (invDependency != null) {
            SecondaryId.createSecondaryId(SimpleSimon.simpleSimon.db,
                    nodeId,
                    SecondaryId.secondaryId(RecreateRole_NodeInstance.INVDEPENDENCY_ID, invDependency));
        }
        Link1Id.createLink1(SimpleSimon.simpleSimon.db,
                nodeId,
                RecreateRole_NodeInstance.ORIGIN_ID,
                originType);
        Link1Id.createLink1(SimpleSimon.simpleSimon.db,
                nodeId,
                RecreateRole_NodeInstance.DESTINATION_ID,
                destinationType);
        if (ofRole != null) {
            Link1Id.createLink1(SimpleSimon.simpleSimon.db,
                    nodeId,
                    RecreateRole_NodeInstance.OFROLE_ID,
                    ofRole);
        }
    }

    public Lnk1_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        throw new UnsupportedOperationException();
    }
}
