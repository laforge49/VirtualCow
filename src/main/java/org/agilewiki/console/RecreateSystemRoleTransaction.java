package org.agilewiki.console;

import org.agilewiki.console.unRole.UnRole;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the unRole.
 */
public class RecreateSystemRoleTransaction extends RecreateRoleTransaction {
    public final static String NAME = "recreateSystemRole";
    public static SystemRole systemRole;
    final public static String SYSTEM_NODE_ID = "$nsystem.node";

    @Override
    public Role role() {
        return systemRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);

        Link1Id.createLink1(db, NODETYPE_KEY_ID, TARGET_ID, NODE_NODE_ID);
        Link1Id.createLink1(db, INVDEPENDENCY_KEY_ID, TARGET_ID, LNK1_NODE_ID);

        Link1Id.createLink1(db, EMAIL_KEY_ID, TARGET_ID, USER_NODE_ID);
        Link1Id.createLink1(db, ROLE_KEY_ID, TARGET_ID, USER_NODE_ID);
        Link1Id.createLink1(db, SUBJECT_KEY_ID, TARGET_ID, NODE_NODE_ID);

        Link1Id.createLink1(db, TARGET_LNK1_ID, ORIGIN_ID, NODE_NODE_ID);
        Link1Id.createLink1(db, TARGET_LNK1_ID, DESTINATION_ID, NODE_NODE_ID);

        Link1Id.createLink1(db, OFROLE_LNK1_ID, ORIGIN_ID, METADATA_NODE_ID);
        Link1Id.createLink1(db, OFROLE_LNK1_ID, DESTINATION_ID, ROLE_NODE_ID);
        SecondaryId.createSecondaryId(db, OFROLE_LNK1_ID, SecondaryId.secondaryId(INVDEPENDENCY_ID, OFROLE_ID));

        Link1Id.createLink1(db, ORIGIN_LNK1_ID, ORIGIN_ID, LNK1_NODE_ID);
        Link1Id.createLink1(db, ORIGIN_LNK1_ID, DESTINATION_ID, NODE_NODE_ID);
        Link1Id.createLink1(db, ORIGIN_LNK1_ID, OFROLE_ID, SYSTEM_NODE_ID);

        Link1Id.createLink1(db, DESTINATION_LNK1_ID, ORIGIN_ID, LNK1_NODE_ID);
        Link1Id.createLink1(db, DESTINATION_LNK1_ID, DESTINATION_ID, NODE_NODE_ID);
        Link1Id.createLink1(db, DESTINATION_LNK1_ID, OFROLE_ID, SYSTEM_NODE_ID);

        Link1Id.createLink1(db, USER_LNK1_ID, ORIGIN_ID, METADATA_NODE_ID);
        Link1Id.createLink1(db, USER_LNK1_ID, DESTINATION_ID, USER_NODE_ID);
        Link1Id.createLink1(db, USER_LNK1_ID, OFROLE_ID, SYSTEM_NODE_ID);

        for (String transactionName: db.transactionRegistry.keySet()) {
            SecondaryId.createSecondaryId(db, "$" + transactionName + ".node", SecondaryId.secondaryId(NODETYPE_ID, METADATA_NODE_ID));
            SecondaryId.createSecondaryId(db, "$" + transactionName + ".node", SecondaryId.secondaryId(NODETYPE_ID, NODE_NODE_ID));
            SecondaryId.createSecondaryId(db, "$" + transactionName + ".node", SecondaryId.secondaryId(NODETYPE_ID, JOURNALENTRY_NODE_ID));
        }
    }
}