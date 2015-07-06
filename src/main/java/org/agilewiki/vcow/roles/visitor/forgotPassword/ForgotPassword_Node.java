package org.agilewiki.vcow.roles.visitor.forgotPassword;

import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.awdb.nodes.Realm_NodeFactory;
import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.VCJournalEntry_Node;

public class ForgotPassword_Node extends VCJournalEntry_Node {
    public final static String NAME = "forgotPassword";

    public ForgotPassword_Node() {
    }

    public ForgotPassword_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public String getRealmId() {
        return Realm_NodeFactory.USER_REALM_ID;
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(NameIds.USER_KEY);
        String passwordHash = (String) mapNode.get(NameIds.PASSWORD_KEY);
        getAwDb().set(userId, NameIds.PASSWORD_KEY, passwordHash);
    }
}
