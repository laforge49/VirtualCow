package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.Node;
import org.agilewiki.utils.ids.composites.Link1Id;

public class Key_Node extends Metadata_Node {
    private static Key_Node key_node;
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

    public static Key_Node get() {
        return key_node;
    }

    public static void create() {
        key_node = new Key_Node(ID, Node_Node.ID);
    }

    public static void define(String nodeId, String targetType) {
        Node_Node.define(nodeId, ID, null);
        Link1Id.createLink1(SimpleSimon.simpleSimon.db,
                nodeId,
                Lnk1_Node.TARGET_ID,
                targetType);
    }

    public Key_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Key_NodeInstance(nodeId, factoryId);
    }
}
