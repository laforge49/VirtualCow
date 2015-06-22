package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.oodb.nodes.*;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class RecreateSystemRole_NodeInstance extends RecreateRole_NodeInstance {
    public final static String NAME = "recreateSystemRole";
    public static System_Role systemRole;

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

        Key_Node.define(Key_Node.NODETYPE_KEY_ID, Node_Node.ID);
        Key_Node.define(Key_Node.SUPERTYPE_KEY_ID, Node_Node.ID);
        Key_Node.define(Key_Node.ATTRIBUTENAME_KEY_ID, Attribute_Node.ID);
        Key_Node.define(Key_Node.INVDEPENDENCY_KEY_ID, Lnk1_Node.ID);

        Key_Node.define(Key_Node.EMAIL_KEY_ID, User_Node.ID);
        Key_Node.define(Key_Node.ROLE_KEY_ID, User_Node.ID);
        Key_Node.define(Key_Node.SUBJECT_KEY_ID, Node_Node.ID);

        Lnk1_Node.define(Lnk1_Node.TARGET_LNK1_ID, null, Node_Node.ID, Node_Node.ID, System_Role.ID);
        Lnk1_Node.define(Lnk1_Node.OFROLE_LNK1_ID, Lnk1_Node.OFROLE_ID, Metadata_Node.ID, Role_Node.ID, null);
        Lnk1_Node.define(Lnk1_Node.ATTRIBUTEOF_LNK1_ID, Lnk1_Node.ATTRIBUTEOF_ID, Attribute_Node.ID, Metadata_Node.ID, null);
        Lnk1_Node.define(Lnk1_Node.ORIGIN_LNK1_ID, null, Lnk1_Node.ID, Node_Node.ID, System_Role.ID);
        Lnk1_Node.define(Lnk1_Node.DESTINATION_LNK1_ID, null, Lnk1_Node.ID, Node_Node.ID, System_Role.ID);

        Lnk1_Node.define(Lnk1_Node.USER_LNK1_ID, null, Metadata_Node.ID, User_Node.ID, System_Role.ID);

        for (String transactionName : db.transactionRegistry.keySet()) {
            Node_Node.define("$n" + transactionName + ".node", Node_Node.ID, JournalEntry_Node.ID);
        }

        Attribute_Node.define("$npassword", User_Node.ID);
    }
}
