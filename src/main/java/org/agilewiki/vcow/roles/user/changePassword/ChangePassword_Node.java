package org.agilewiki.vcow.roles.user.changePassword;

import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.VCJournalEntry_Node;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Changes the user's password.
 */
public class ChangePassword_Node extends VCJournalEntry_Node {
    public final static String NAME = "changePassword";

    public ChangePassword_Node() {
    }

    public ChangePassword_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(NameIds.USER_KEY);
        String passwordHash = (String) mapNode.get(NameIds.PASSWORD_KEY);
        getAwDb().set(userId, NameIds.PASSWORD_KEY, passwordHash);
    }
}
