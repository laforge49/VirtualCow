package org.agilewiki.console;

import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the common metadata for a role.
 */
abstract public class RecreateRoleTransaction extends VCTransaction {
    final public String METADATA_NODE_ID = "$nmetadata.node";

    final public String NODE_NODE_ID = "$nnode.node";
    final public String KEY_NODE_ID = "$nkey.node";
    final public String LNK1_NODE_ID = "$nlnk1.node";

    final public String ROLE_NODE_ID = "$nrole.node";
    final public String USER_NODE_ID = "$nuser.node";
    final public String JOURNALENTRY_NODE_ID = "$njournalEntry.node";

    final public String NODETYPE_ID = "$nodeType";
    final public String NODETYPE_KEY_ID = "$nnodeType.key";
    final public String INVDEPENDENCY_ID = "$ninvDependency";
    final public String INVDEPENDENCY_KEY_ID = "$ninvDependency.key";

    final public String EMAIL_ID = "$nemail";
    final public String EMAIL_KEY_ID = "$nemail.key";
    final public String SUBJECT_ID = "$nsubject";
    final public String SUBJECT_KEY_ID = "$nsubject.key";

    final public String TARGET_ID = "$ntarget";
    final public String TARGET_LNK1_ID = "$ntarget.lnk1";

    final public String OFROLE_ID = "$nofRole";
    final public String OFROLE_LNK1_ID = "$nofRole.lnk1";
    final public String ORIGIN_ID = "$norigin";
    final public String ORIGIN_LNK1_ID = "$norigin.lnk1";
    final public String DESTINATION_ID = "$ndestination";
    final public String DESTINATION_LNK1_ID = "$ndestination.lnk1";

    final public String USER_ID = "$nuser";
    final public String USER_LNK1_ID = "$nuser.lnk1";

    abstract public Role role();

    @Override
    public void process(Db db, MapNode tMapNode) {
        final String thisRoleId = "$n" + role().roleName();
        final String thisRoleNodeId = thisRoleId + ".node";

        Delete.delete(db, thisRoleNodeId);

        SecondaryId.createSecondaryId(db, METADATA_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));

        SecondaryId.createSecondaryId(db, NODE_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, ROLE_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, ROLE_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, USER_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, USER_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, JOURNALENTRY_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, JOURNALENTRY_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));

        SecondaryId.createSecondaryId(db, KEY_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, NODETYPE_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, NODETYPE_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, KEY_NODE_ID));
        SecondaryId.createSecondaryId(db, INVDEPENDENCY_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, INVDEPENDENCY_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, KEY_NODE_ID));

        SecondaryId.createSecondaryId(db, EMAIL_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, EMAIL_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, KEY_NODE_ID));
        SecondaryId.createSecondaryId(db, SUBJECT_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, SUBJECT_KEY_ID, SecondaryId.secondaryId(NODETYPE_ID, KEY_NODE_ID));

        SecondaryId.createSecondaryId(db, LNK1_NODE_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, TARGET_LNK1_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, TARGET_LNK1_ID, SecondaryId.secondaryId(NODETYPE_ID, LNK1_NODE_ID));
        SecondaryId.createSecondaryId(db, ORIGIN_LNK1_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, ORIGIN_LNK1_ID, SecondaryId.secondaryId(NODETYPE_ID, LNK1_NODE_ID));
        SecondaryId.createSecondaryId(db, DESTINATION_LNK1_ID, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, DESTINATION_LNK1_ID, SecondaryId.secondaryId(NODETYPE_ID, LNK1_NODE_ID));

        SecondaryId.createSecondaryId(db, thisRoleNodeId, SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
        SecondaryId.createSecondaryId(db, thisRoleNodeId, SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
        SecondaryId.createSecondaryId(db, thisRoleNodeId, SecondaryId.secondaryId(NODETYPE_ID, ROLE_NODE_ID));

        //Move the rest to the system role.

    }
}
