package org.agilewiki.console.profile;

import org.agilewiki.console.User;
import org.agilewiki.console.transactions.VCTransaction;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Changes the user's password.
 */
public class NewEmailAddressTransaction extends VCTransaction {
    public final static String NAME = "newEmailAddress";

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(User.USER_KEY);

        String oldEmailAddressId = ValueId.generate(User.email(db, userId, db.getTimestamp()));
        String oldSecondaryId = SecondaryId.secondaryId(User.EMAIL_ID, oldEmailAddressId);
        SecondaryId.removeSecondaryId(db, userId, oldSecondaryId);

        String emailAddressId = ValueId.generate((String) mapNode.get(User.EMAIL_ID));
        String secondaryId = SecondaryId.secondaryId(User.EMAIL_ID, emailAddressId);
        SecondaryId.createSecondaryId(db, userId, secondaryId);
    }
}
