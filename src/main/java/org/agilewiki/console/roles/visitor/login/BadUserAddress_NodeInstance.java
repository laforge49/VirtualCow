package org.agilewiki.console.roles.visitor.login;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.oodb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class BadUserAddress_NodeInstance extends JournalEntry_NodeInstance {
    public final static String NAME = "badUserAddress";

    public BadUserAddress_NodeInstance() {
    }

    public BadUserAddress_NodeInstance(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String jeName = db.getJEName();
        String subject = (String) mapNode.get(NameIds.SUBJECT);
        if (subject != null) {
            String subjectVId = ValueId.generate(subject.toLowerCase());
            getOoDb().createSecondaryId(jeName, NameIds.SUBJECT, subjectVId);
        }
    }
}