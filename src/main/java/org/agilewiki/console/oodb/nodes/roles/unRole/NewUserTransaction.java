package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.User;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.collections.ListNode;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.List;
import java.util.Map;

/**
 * Create a new user.
 */
public class NewUserTransaction extends JournalEntry_Node {
    public final static String NAME = "newUser";

    public NewUserTransaction() {
    }

    public NewUserTransaction(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(User.USER_KEY);
        String emailId = (String) mapNode.get(User.EMAIL_ID);
        String passwordHash = (String) mapNode.get(User.PASSWORD_KEY);
        ListNode list = mapNode.getList(User.ROLE_ID);
        List<String> flatList = list.flatList();
        String[] roles = new String[]{};
        User.createUser(db, userId, emailId, passwordHash, flatList.toArray(roles));
    }
}
