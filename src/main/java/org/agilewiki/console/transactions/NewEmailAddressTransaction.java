package org.agilewiki.console.transactions;

import org.agilewiki.console.User;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

/**
 * Changes the user's password.
 */
public class NewEmailAddressTransaction implements Transaction {
    public final static String NAME = "newEmailAddress";

    public String update(Db db, String userId, String emailAddress)
            throws Exception {
        MapNode mn = db.dbFactoryRegistry.nilMap;
        mn = mn.add(User.USER_KEY, userId);
        mn = mn.add(User.EMAIL_ID, emailAddress);
        return db.update(NAME, mn).call();
    }

    @Override
    public void transform(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(User.USER_KEY);

        String oldEmailAddressId = ValueId.generate(User.email(db, userId, db.getTimestamp()));
        String oldSecondaryId = SecondaryId.secondaryId(User.EMAIL_ID, oldEmailAddressId);
        SecondaryId.removeSecondaryId(db, userId, oldSecondaryId);

        String emailAddressId = ValueId.generate((String) mapNode.get(User.EMAIL_ID));
        String secondaryId = SecondaryId.secondaryId(User.EMAIL_ID, emailAddressId);
        SecondaryId.createSecondaryId(db, userId, secondaryId);
    }
}
