package org.agilewiki.console.roles.user.changePassword;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeInstance;
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
        String userId = (String) mapNode.get(NameIds.USER_KEY);
        String passwordHash = (String) mapNode.get(NameIds.PASSWORD_KEY);
        getOoDb().set(userId, NameIds.PASSWORD_KEY, passwordHash);
    }
}
