package org.agilewiki.console.oodb.nodes.roles.user.changePassword;

import org.agilewiki.console.User;
import org.agilewiki.console.oodb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Changes the user's password.
 */
public class ChangePassword_NodeInstance extends JournalEntry_NodeInstance {
    public final static String NAME = "changePassword";

    public ChangePassword_NodeInstance() {
    }

    public ChangePassword_NodeInstance(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(User.USER_KEY);
        String passwordHash = (String) mapNode.get(User.PASSWORD_KEY);
        getOoDb().set(userId, User.PASSWORD_KEY, passwordHash);
    }
}
