package org.agilewiki.console.user;

import org.agilewiki.console.User;
import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Changes the user's password.
 */
public class ChangePasswordTransaction extends VCTransaction {
    public final static String NAME = "changePassword";

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(User.USER_KEY);
        String passwordHash = (String) mapNode.get(User.PASSWORD_KEY);
        db.set(userId, User.PASSWORD_KEY, passwordHash);
    }
}
