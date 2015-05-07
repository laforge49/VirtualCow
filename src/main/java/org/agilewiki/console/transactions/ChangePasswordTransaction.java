package org.agilewiki.console.transactions;

import org.agilewiki.console.User;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

/**
 * Changes the user's password.
 */
public class ChangePasswordTransaction implements Transaction {
    public final static String NAME = "changePassword";

    public String update(Db db, String userId, String passwordHash)
            throws Exception {
        MapNode mn = db.dbFactoryRegistry.nilMap;
        mn = mn.add(User.USER_KEY, userId);
        mn = mn.add(User.PASSWORD_KEY, passwordHash);
        return db.update(NAME, mn).call();
    }

    @Override
    public void transform(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(User.USER_KEY);
        String passwordHash = (String) mapNode.get(User.PASSWORD_KEY);
        db.set(userId, User.PASSWORD_KEY, passwordHash);
    }
}
