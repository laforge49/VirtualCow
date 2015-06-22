package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.oodb.nodes.Key_Node;
import org.agilewiki.console.oodb.nodes.Lnk1_Node;
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

        Lnk1_Node.define(TARGET_LNK1_ID, null, NODE_NODE_ID, NODE_NODE_ID, SYSTEM_NODE_ID);
        Lnk1_Node.define(OFROLE_LNK1_ID, OFROLE_ID, METADATA_NODE_ID, ROLE_NODE_ID, null);
        Lnk1_Node.define(ORIGIN_LNK1_ID, null, LNK1_NODE_ID, NODE_NODE_ID, SYSTEM_NODE_ID);
        Lnk1_Node.define(DESTINATION_LNK1_ID, null, LNK1_NODE_ID, NODE_NODE_ID, SYSTEM_NODE_ID);

        Lnk1_Node.define(USER_LNK1_ID, null, METADATA_NODE_ID, USER_NODE_ID, SYSTEM_NODE_ID);

        for (String transactionName : db.transactionRegistry.keySet()) {
            Node_Node.define("$n" + transactionName + ".node", NODE_NODE_ID, JOURNALENTRY_NODE_ID);
        }
    }
}
