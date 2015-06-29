package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.utils.immutable.FactoryRegistry;

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
        lnk1_node = new Lnk1_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public static void define(String nodeId, String invDependency, String originType, String destinationType, String ofRole) {
        OODb ooDb = SimpleSimon.simpleSimon.ooDb;
        Node_Node.define(nodeId, ID, null, ofRole);
        if (invDependency != null) {
            ooDb.createSecondaryId(nodeId, Key_Node.INVDEPENDENCY_ID, invDependency);
        }
        ooDb.createLnk1(nodeId,
                Lnk1_Node.ORIGIN_ID,
                originType);
        ooDb.createLnk1(nodeId,
                Lnk1_Node.DESTINATION_ID,
                destinationType);
    }

    public Lnk1_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Lnk1_NodeInstance(nodeId, timestamp);
    }
}
