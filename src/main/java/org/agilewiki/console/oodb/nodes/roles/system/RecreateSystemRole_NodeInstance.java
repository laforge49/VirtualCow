package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.oodb.nodes.Key_Node;
import org.agilewiki.console.oodb.nodes.Node_Node;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class RecreateSystemRole_NodeInstance extends RecreateRole_NodeInstance {
    public final static String NAME = "recreateSystemRole";
    public static SystemRole_Node systemRole;
    final public static String SYSTEM_NODE_ID = "$nsystem.node";

    public RecreateSystemRole_NodeInstance() {
    }

    public RecreateSystemRole_NodeInstance(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Role role() {
        return systemRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);

        Key_Node.define(NODETYPE_KEY_ID, NODE_NODE_ID);
        Key_Node.define(SUPERTYPE_KEY_ID, NODE_NODE_ID);
        Key_Node.define(INVDEPENDENCY_KEY_ID, LNK1_NODE_ID);

        Key_Node.define(EMAIL_KEY_ID, USER_NODE_ID);
        Key_Node.define(ROLE_KEY_ID, USER_NODE_ID);
        Key_Node.define(SUBJECT_KEY_ID, NODE_NODE_ID);

        Link1Id.createLink1(db, TARGET_LNK1_ID, ORIGIN_ID, NODE_NODE_ID);
        Link1Id.createLink1(db, TARGET_LNK1_ID, DESTINATION_ID, NODE_NODE_ID);
        Link1Id.createLink1(db, TARGET_LNK1_ID, OFROLE_ID, SYSTEM_NODE_ID);

        Link1Id.createLink1(db, OFROLE_LNK1_ID, ORIGIN_ID, METADATA_NODE_ID);
        Link1Id.createLink1(db, OFROLE_LNK1_ID, DESTINATION_ID, ROLE_NODE_ID);

        Link1Id.createLink1(db, ORIGIN_LNK1_ID, ORIGIN_ID, LNK1_NODE_ID);
        Link1Id.createLink1(db, ORIGIN_LNK1_ID, DESTINATION_ID, NODE_NODE_ID);
        Link1Id.createLink1(db, ORIGIN_LNK1_ID, OFROLE_ID, SYSTEM_NODE_ID);

        Link1Id.createLink1(db, DESTINATION_LNK1_ID, ORIGIN_ID, LNK1_NODE_ID);
        Link1Id.createLink1(db, DESTINATION_LNK1_ID, DESTINATION_ID, NODE_NODE_ID);
        Link1Id.createLink1(db, DESTINATION_LNK1_ID, OFROLE_ID, SYSTEM_NODE_ID);

        Link1Id.createLink1(db, USER_LNK1_ID, ORIGIN_ID, METADATA_NODE_ID);
        Link1Id.createLink1(db, USER_LNK1_ID, DESTINATION_ID, USER_NODE_ID);
        Link1Id.createLink1(db, USER_LNK1_ID, OFROLE_ID, SYSTEM_NODE_ID);

        for (String transactionName : db.transactionRegistry.keySet()) {
            Node_Node.define("$n" + transactionName + ".node", NODE_NODE_ID, JOURNALENTRY_NODE_ID);
        }
    }
}
