package org.agilewiki.console.oodb.nodes.roles.user.newEmailAddress;

import org.agilewiki.console.User;
import org.agilewiki.console.oodb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Changes the user's password.
 */
public class NewEmailAddress_NodeInstance extends JournalEntry_NodeInstance {
    public final static String NAME = "newEmailAddress";

    public NewEmailAddress_NodeInstance() {
    }

    public NewEmailAddress_NodeInstance(String nodeId) {
        super(nodeId);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(User.USER_KEY);

        String oldEmailAddressId = ValueId.generate(User.email(userId, ooDb.getTimestamp()));
        ooDb.removeSecondaryId(userId, User.EMAIL_ID, oldEmailAddressId);

        String emailAddressId = ValueId.generate((String) mapNode.get(User.EMAIL_ID));
        ooDb.createSecondaryId(userId, User.EMAIL_ID, emailAddressId);
    }
}
