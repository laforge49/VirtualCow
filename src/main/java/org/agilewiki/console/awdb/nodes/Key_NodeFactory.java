package org.agilewiki.console.awdb.nodes;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Key_NodeFactory extends Metadata_NodeFactory {
    public final static String ID = "$nkey.node";

    final public static String NODETYPE_ID = "$nnodeType";
    final public static String NODETYPE_KEY_ID = "$nnodeType.key";
    final public static String SUPERTYPE_ID = "$nsuperType";
    final public static String SUPERTYPE_KEY_ID = "$nsuperType.key";
    final public static String INVDEPENDENCY_ID = "$ninvDependency";
    final public static String INVDEPENDENCY_KEY_ID = "$ninvDependency.key";
    final public static String ATTRIBUTENAME_ID = "$nattributeName";
    final public static String ATTRIBUTENAME_KEY_ID = "$nattributeName.key";

    final public static String EMAIL_ID = "$nemail";
    final public static String EMAIL_KEY_ID = "$nemail.key";
    final public static String ROLE_ID = "$nrole";
    final public static String ROLE_KEY_ID = "$nrole.key";
    final public static String SUBJECT_ID = "$nsubject";
    final public static String SUBJECT_KEY_ID = "$nsubject.key";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new Key_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public static void define(String nodeId, String targetType, String roleId) {
        Node_NodeFactory.define(nodeId, ID, null, roleId);
        getAwDb().createLnk1(nodeId,
                Lnk1_NodeFactory.TARGET_ID,
                targetType);
    }

    public Key_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Key_Node(nodeId, timestamp);
    }
}
