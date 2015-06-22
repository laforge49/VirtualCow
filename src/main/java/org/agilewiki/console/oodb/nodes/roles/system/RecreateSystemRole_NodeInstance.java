package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.oodb.nodes.*;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class RecreateSystemRole_NodeInstance extends RecreateRole_NodeInstance {
    public final static String NAME = "recreateSystemRole";
    public static SystemRole_Node systemRole;
    final public static String SYSTEM_NODE_ID = "$nsystemRole.node";

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

        Key_Node.define(NODETYPE_KEY_ID, Node_Node.ID);
        Key_Node.define(SUPERTYPE_KEY_ID, Node_Node.ID);
        Key_Node.define(INVDEPENDENCY_KEY_ID, Lnk1_Node.ID);

        Key_Node.define(EMAIL_KEY_ID, User_Node.ID);
        Key_Node.define(ROLE_KEY_ID, User_Node.ID);
        Key_Node.define(SUBJECT_KEY_ID, Node_Node.ID);

        Lnk1_Node.define(TARGET_LNK1_ID, null, Node_Node.ID, Node_Node.ID, SYSTEM_NODE_ID);
        Lnk1_Node.define(OFROLE_LNK1_ID, OFROLE_ID, Metadata_Node.ID, Role_Node.ID, null);
        Lnk1_Node.define(ORIGIN_LNK1_ID, null, Lnk1_Node.ID, Node_Node.ID, SYSTEM_NODE_ID);
        Lnk1_Node.define(DESTINATION_LNK1_ID, null, Lnk1_Node.ID, Node_Node.ID, SYSTEM_NODE_ID);

        Lnk1_Node.define(USER_LNK1_ID, null, Metadata_Node.ID, User_Node.ID, SYSTEM_NODE_ID);

        for (String transactionName : db.transactionRegistry.keySet()) {
            Node_Node.define("$n" + transactionName + ".node", Node_Node.ID, JournalEntry_Node.ID);
        }
    }
}
