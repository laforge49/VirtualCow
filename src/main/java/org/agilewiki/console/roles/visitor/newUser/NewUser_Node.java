package org.agilewiki.console.roles.visitor.newUser;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.User_NodeInstance;
import org.agilewiki.console.awdb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.collections.ListNode;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.List;

/**
 * Create a new user.
 */
public class NewUser_Node extends JournalEntry_Node {
    public final static String NAME = "newUser";

    public NewUser_Node() {
    }

    public NewUser_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String userId = (String) mapNode.get(NameIds.USER_KEY);
        String emailId = (String) mapNode.get(NameIds.EMAIL_ID);
        String passwordHash = (String) mapNode.get(NameIds.PASSWORD_KEY);
        ListNode list = mapNode.getList(NameIds.ROLE_ID);
        List<String> flatList = list.flatList();
        String[] roles = new String[]{};
        User_NodeInstance.createUser(userId, emailId, passwordHash, flatList.toArray(roles));
    }
}
