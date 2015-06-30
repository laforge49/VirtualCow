package org.agilewiki.console.roles.user.newEmailAddress;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.User_NodeInstance;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Changes the user's password.
 */
public class NewEmailAddress_NodeInstance extends JournalEntry_NodeInstance {
    public final static String NAME = "newEmailAddress";

    public NewEmailAddress_NodeInstance() {
    }

    public NewEmailAddress_NodeInstance(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(NameIds.USER_KEY);

        User_NodeInstance user_nodeInstance = (User_NodeInstance) getOoDb().fetchNode(userId, getOoDb().getDbTimestamp());
        String oldEmailAddressId = ValueId.generate(user_nodeInstance.getEmailAddress());
        getOoDb().removeSecondaryId(userId, NameIds.EMAIL_ID, oldEmailAddressId);

        String emailAddressId = ValueId.generate((String) mapNode.get(NameIds.EMAIL_ID));
        getOoDb().createSecondaryId(userId, NameIds.EMAIL_ID, emailAddressId);
    }
}
