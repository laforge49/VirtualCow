package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.Node;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;

public class Lnk1_Node extends Metadata_Node {
    private static Lnk1_Node lnk1_node;
    public final static String ID = "$nlnk1.node";

    final public static String TARGET_ID = "$ntarget";
    final public static String TARGET_LNK1_ID = "$ntarget.lnk1";

    final public static String OFROLE_ID = "$nofRole";
    final public static String OFROLE_LNK1_ID = "$nofRole.lnk1";
    final public static String ATTRIBUTEOF_ID = "$nattributeOf";
    final public static String ATTRIBUTEOF_LNK1_ID = "$nattributeOf.lnk1";
    final public static String ORIGIN_ID = "$norigin";
    final public static String ORIGIN_LNK1_ID = "$norigin.lnk1";
    final public static String DESTINATION_ID = "$ndestination";
    final public static String DESTINATION_LNK1_ID = "$ndestination.lnk1";

    final public static String USER_ID = "$nuser";
    final public static String USER_LNK1_ID = "$nuser.lnk1";

    public static Lnk1_Node get() {
        return lnk1_node;
    }

    public static void create() {
        lnk1_node = new Lnk1_Node(ID);
    }

    public static void define(String nodeId, String invDependency, String originType, String destinationType, String ofRole) {
        Node_Node.define(nodeId, ID, null, ofRole);
        if (invDependency != null) {
            SecondaryId.createSecondaryId(SimpleSimon.simpleSimon.db,
                    nodeId,
                    SecondaryId.secondaryId(Key_Node.INVDEPENDENCY_ID, invDependency));
        }
        Link1Id.createLink1(SimpleSimon.simpleSimon.db,
                nodeId,
                Lnk1_Node.ORIGIN_ID,
                originType);
        Link1Id.createLink1(SimpleSimon.simpleSimon.db,
                nodeId,
                Lnk1_Node.DESTINATION_ID,
                destinationType);
    }

    public Lnk1_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        throw new UnsupportedOperationException();
    }
}
