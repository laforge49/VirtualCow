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

    @Override
    public void transform(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(User.USER_KEY);
        String passwordHash = (String) mapNode.get(User.PASSWORD_KEY);
        db.set(userId, User.PASSWORD_KEY, passwordHash);
    }
}
