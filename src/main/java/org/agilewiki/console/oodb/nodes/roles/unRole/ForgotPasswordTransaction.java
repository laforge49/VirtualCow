package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.User;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.Map;

/**
 * Changes the user's password.
 */
public class ForgotPasswordTransaction extends JournalEntry_Node {
    public final static String NAME = "forgotPassword";

    public ForgotPasswordTransaction() {
    }

    public ForgotPasswordTransaction(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(User.USER_KEY);
        String passwordHash = (String) mapNode.get(User.PASSWORD_KEY);
        db.set(userId, User.PASSWORD_KEY, passwordHash);
    }
}
