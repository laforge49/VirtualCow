package org.agilewiki.vcow.roles.visitor.login;

import org.agilewiki.awdb.db.ids.ValueId;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.VCJournalEntry_Node;

public class BadUserAddress_Node extends VCJournalEntry_Node {
    public final static String NAME = "badUserAddress";

    public BadUserAddress_Node() {
    }

    public BadUserAddress_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String jeName = db.getJEName();
        String subject = (String) mapNode.get(NameIds.SUBJECT);
        if (subject != null) {
            String subjectVId = ValueId.generate(subject.toLowerCase());
            getAwDb().createSecondaryId(jeName, NameIds.SUBJECT, subjectVId);
        }
    }
}
