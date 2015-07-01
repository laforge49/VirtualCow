package org.agilewiki.vcow.roles.visitor.forgotPassword;

import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.VCJournalEntry_Node;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class ForgotPassword_Node extends VCJournalEntry_Node {
    public final static String NAME = "forgotPassword";

    public ForgotPassword_Node() {
    }

    public ForgotPassword_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(NameIds.USER_KEY);
        String passwordHash = (String) mapNode.get(NameIds.PASSWORD_KEY);
        getAwDb().set(userId, NameIds.PASSWORD_KEY, passwordHash);
    }
}
