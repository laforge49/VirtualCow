package org.agilewiki.vcow.roles.visitor.newUser;

import org.agilewiki.awdb.db.immutable.collections.ListNode;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.VCUser_Node;
import org.agilewiki.vcow.VCJournalEntry_Node;

import java.util.List;

/**
 * Create a new user.
 */
public class NewUser_Node extends VCJournalEntry_Node {
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
        VCUser_Node.createUser(userId, emailId, passwordHash, flatList.toArray(roles));
    }
}
