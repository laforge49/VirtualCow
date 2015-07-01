package org.agilewiki.vcow.roles.user.newEmailAddress;

import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.User_NodeInstance;
import org.agilewiki.vcow.VCJournalEntry_Node;
import org.agilewiki.awdb.db.ids.ValueId;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;

/**
 * Changes the user's password.
 */
public class NewEmailAddress_Node extends VCJournalEntry_Node {
    public final static String NAME = "newEmailAddress";

    public NewEmailAddress_Node() {
    }

    public NewEmailAddress_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(NameIds.USER_KEY);

        User_NodeInstance user_nodeInstance = (User_NodeInstance) getAwDb().fetchNode(userId, getAwDb().getDbTimestamp());
        String oldEmailAddressId = ValueId.generate(user_nodeInstance.getEmailAddress());
        getAwDb().removeSecondaryId(userId, NameIds.EMAIL_ID, oldEmailAddressId);

        String emailAddressId = ValueId.generate((String) mapNode.get(NameIds.EMAIL_ID));
        getAwDb().createSecondaryId(userId, NameIds.EMAIL_ID, emailAddressId);
    }
}
