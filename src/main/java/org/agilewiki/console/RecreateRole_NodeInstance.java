package org.agilewiki.console;

import org.agilewiki.console.oodb.nodes.*;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the common metadata for a role.
 */
abstract public class RecreateRole_NodeInstance extends JournalEntry_NodeInstance {
    final public static String NODETYPE_ID = "$nnodeType";
    final public static String NODETYPE_KEY_ID = "$nnodeType.key";
    final public static String SUPERTYPE_ID = "$nsuperType";
    final public static String SUPERTYPE_KEY_ID = "$nsuperType.key";
    final public static String INVDEPENDENCY_ID = "$ninvDependency";
    final public static String INVDEPENDENCY_KEY_ID = "$ninvDependency.key";

    final public static String EMAIL_ID = "$nemail";
    final public static String EMAIL_KEY_ID = "$nemail.key";
    final public static String ROLE_ID = "$nrole";
    final public static String ROLE_KEY_ID = "$nrole.key";
    final public static String SUBJECT_ID = "$nsubject";
    final public static String SUBJECT_KEY_ID = "$nsubject.key";

    final public static String TARGET_ID = "$ntarget";
    final public static String TARGET_LNK1_ID = "$ntarget.lnk1";

    final public static String OFROLE_ID = "$nofRole";
    final public static String OFROLE_LNK1_ID = "$nofRole.lnk1";
    final public static String ORIGIN_ID = "$norigin";
    final public static String ORIGIN_LNK1_ID = "$norigin.lnk1";
    final public static String DESTINATION_ID = "$ndestination";
    final public static String DESTINATION_LNK1_ID = "$ndestination.lnk1";

    final public static String USER_ID = "$nuser";
    final public static String USER_LNK1_ID = "$nuser.lnk1";

    public RecreateRole_NodeInstance() {
    }

    public RecreateRole_NodeInstance(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    abstract public Role role();

    @Override
    public void process(Db db, MapNode tMapNode) {
        final String thisRoleId = "$n" + role().roleName() + "Role";
        final String thisRoleNodeId = thisRoleId + ".node";

        Delete.delete(db, thisRoleNodeId);

        Node_Node.define(Metadata_Node.ID, Node_Node.ID, null);

        Node_Node.define(Node_Node.ID, Node_Node.ID, Metadata_Node.ID);
        Node_Node.define(Key_Node.ID, Node_Node.ID, Metadata_Node.ID);
        Node_Node.define(Lnk1_Node.ID, Node_Node.ID, Metadata_Node.ID);

        Node_Node.define(Role_Node.ID, Node_Node.ID, Node_Node.ID);
        Node_Node.define(User_Node.ID, Node_Node.ID, Node_Node.ID);
        Node_Node.define(JournalEntry_Node.ID, Node_Node.ID, Node_Node.ID);

        SecondaryId.createSecondaryId(db, thisRoleNodeId, SecondaryId.secondaryId(NODETYPE_ID, Role_Node.ID));
    }
}
