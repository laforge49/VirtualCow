package org.agilewiki.console.awdb.nodes;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Lnk1_NodeFactory extends Metadata_NodeFactory {
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

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new Lnk1_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public static void define(String nodeId, String invDependency, String originType, String destinationType, String ofRole) {
        AwDb awDb = getOoDb();
        Node_NodeFactory.define(nodeId, ID, null, ofRole);
        if (invDependency != null) {
            awDb.createSecondaryId(nodeId, Key_NodeFactory.INVDEPENDENCY_ID, invDependency);
        }
        awDb.createLnk1(nodeId,
                Lnk1_NodeFactory.ORIGIN_ID,
                originType);
        awDb.createLnk1(nodeId,
                Lnk1_NodeFactory.DESTINATION_ID,
                destinationType);
    }

    public Lnk1_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Lnk1_NodeInstance(nodeId, timestamp);
    }
}
