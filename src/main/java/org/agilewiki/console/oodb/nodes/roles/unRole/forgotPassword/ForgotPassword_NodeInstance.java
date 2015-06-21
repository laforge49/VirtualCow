package org.agilewiki.console.oodb.nodes.roles.unRole.forgotPassword;

import org.agilewiki.console.User;
import org.agilewiki.console.oodb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class ForgotPassword_NodeInstance extends JournalEntry_NodeInstance {
    public final static String NAME = "forgotPassword";

    public ForgotPassword_NodeInstance() {
    }

    public ForgotPassword_NodeInstance(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(User.USER_KEY);
        String passwordHash = (String) mapNode.get(User.PASSWORD_KEY);
        db.set(userId, User.PASSWORD_KEY, passwordHash);
    }
}
