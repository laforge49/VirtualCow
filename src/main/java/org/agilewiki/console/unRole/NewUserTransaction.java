package org.agilewiki.console.unRole;

import org.agilewiki.console.User;
import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Create a new user.
 */
public class NewUserTransaction extends VCTransaction {
    public final static String NAME = "newUser";

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(User.USER_KEY);
        String emailId = (String) mapNode.get(User.EMAIL_ID);
        String passwordHash = (String) mapNode.get(User.PASSWORD_KEY);
        String userTypeId = (String) mapNode.get(User.USER_TYPE_ID);
        User.createUser(db, userId, emailId, passwordHash, userTypeId);
    }
}