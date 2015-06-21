package org.agilewiki.console;

import org.agilewiki.console.oodb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the common metadata for a role.
 */
abstract public class RecreateRole_NodeInstance extends JournalEntry_NodeInstance {
    final public static String METADATA_NODE_ID = "$nmetadata.node";

    final public static String NODE_NODE_ID = "$nnode.node";
    final public static String KEY_NODE_ID = "$nkey.node";
    final public static String LNK1_NODE_ID = "$nlnk1.node";

    final public static String ROLE_NODE_ID = "$nrole.node";
    final public static String USER_NODE_ID = "$nuser.node";
    final public static String JOURNALENTRY_NODE_ID = "$njournalEntry.node";

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

        SecondaryId.createSecondaryId(db, METADATA_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));

        SecondaryId.createSecondaryId(db, NODE_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, NODE_NODE_ID, SecondaryId.secondaryId(SUPERTYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, KEY_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, KEY_NODE_ID, SecondaryId.secondaryId(SUPERTYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, LNK1_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, LNK1_NODE_ID, SecondaryId.secondaryId(SUPERTYPE_ID, METADATA_NODE_ID));

        SecondaryId.createSecondaryId(db, ROLE_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, ROLE_NODE_ID, SecondaryId.secondaryId(SUPERTYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, USER_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, USER_NODE_ID, SecondaryId.secondaryId(SUPERTYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, JOURNALENTRY_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, JOURNALENTRY_NODE_ID, SecondaryId.secondaryId(SUPERTYPE_ID, NODE_NODE_ID));

        SecondaryId.createSecondaryId(db, NODETYPE_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, NODETYPE_KEY_ID, SecondaryId.secondaryId(SUPERTYPE_ID, KEY_NODE_ID));
        SecondaryId.createSecondaryId(db, SUPERTYPE_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, SUPERTYPE_KEY_ID, SecondaryId.secondaryId(SUPERTYPE_ID, KEY_NODE_ID));
        SecondaryId.createSecondaryId(db, INVDEPENDENCY_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, INVDEPENDENCY_KEY_ID, SecondaryId.secondaryId(SUPERTYPE_ID, KEY_NODE_ID));

        SecondaryId.createSecondaryId(db, EMAIL_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, EMAIL_KEY_ID, SecondaryId.secondaryId(SUPERTYPE_ID, KEY_NODE_ID));
        SecondaryId.createSecondaryId(db, ROLE_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, ROLE_KEY_ID, SecondaryId.secondaryId(SUPERTYPE_ID, KEY_NODE_ID));
        SecondaryId.createSecondaryId(db, SUBJECT_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, SUBJECT_KEY_ID, SecondaryId.secondaryId(SUPERTYPE_ID, KEY_NODE_ID));

        SecondaryId.createSecondaryId(db, TARGET_LNK1_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, TARGET_LNK1_ID, SecondaryId.secondaryId(SUPERTYPE_ID, LNK1_NODE_ID));
        SecondaryId.createSecondaryId(db, OFROLE_LNK1_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, OFROLE_LNK1_ID, SecondaryId.secondaryId(SUPERTYPE_ID, LNK1_NODE_ID));
        SecondaryId.createSecondaryId(db, OFROLE_LNK1_ID, SecondaryId.secondaryId(INVDEPENDENCY_ID, OFROLE_ID));
        SecondaryId.createSecondaryId(db, ORIGIN_LNK1_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, ORIGIN_LNK1_ID, SecondaryId.secondaryId(SUPERTYPE_ID, LNK1_NODE_ID));
        SecondaryId.createSecondaryId(db, DESTINATION_LNK1_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, DESTINATION_LNK1_ID, SecondaryId.secondaryId(SUPERTYPE_ID, LNK1_NODE_ID));

        SecondaryId.createSecondaryId(db, USER_LNK1_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, USER_LNK1_ID, SecondaryId.secondaryId(SUPERTYPE_ID, LNK1_NODE_ID));

        SecondaryId.createSecondaryId(db, thisRoleNodeId, SecondaryId.secondaryId(NODETYPE_ID, ROLE_NODE_ID));
    }
}
