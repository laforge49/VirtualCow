package org.agilewiki.vcow.roles.visitor.login;

import org.agilewiki.awdb.db.ids.ValueId;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.awdb.nodes.Realm_NodeFactory;
import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.VCJournalEntry_Node;

public class Login_Node extends VCJournalEntry_Node {
    public final static String NAME = "login";

    public Login_Node() {
    }

    public Login_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public String getRealmId() {
        return Realm_NodeFactory.USER_REALM_ID;
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String jeName = db.getJEName();
        String subject = (String) mapNode.get(NameIds.SUBJECT);
        if (subject != null) {
            String subjectVId = ValueId.generate(subject.toLowerCase());
            createSecondaryId(NameIds.SUBJECT, subjectVId);
        }
    }
}
