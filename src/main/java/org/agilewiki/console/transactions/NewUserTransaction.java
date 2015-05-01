package org.agilewiki.console.transactions;

import org.agilewiki.console.User;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

/**
 * Create a new user.
 */
public class NewUserTransaction implements Transaction {
    public final static String NAME = "newUser";

    public String update(Db db,
                         String userId,
                         String email,
                         String passwordHash,
                         String userTypeId)
            throws Exception {
        MapNode mn = db.dbFactoryRegistry.nilMap;
        mn = mn.add(User.USER_KEY, userId);
        String emailId = ValueId.generate(email);
        mn = mn.add(User.EMAIL_ID, emailId);
        mn = mn.add(User.PASSWORD_KEY, passwordHash);
        mn = mn.add(User.USER_TYPE_ID, userTypeId);
        return db.update(NAME, mn).call();
    }

    @Override
    public void transform(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(User.USER_KEY);
        String emailId = (String) mapNode.get(User.EMAIL_ID);
        String passwordHash = (String) mapNode.get(User.PASSWORD_KEY);
        String userTypeId = (String) mapNode.get(User.USER_TYPE_ID);
        User.createUser(db, userId, emailId, passwordHash, userTypeId);
    }
}
