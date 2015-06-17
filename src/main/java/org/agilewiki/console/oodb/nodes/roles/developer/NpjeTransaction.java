package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.Map;

/**
 * Non-performing Journal Entry.
 */
public class NpjeTransaction extends VCTransaction {
    public final static String NAME = "npje";

    public NpjeTransaction() {
    }

    public NpjeTransaction(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String jeName = db.getJEName();
        String subject = (String) mapNode.get(NameIds.SUBJECT);
        if (subject != null) {
            String subjectVId = ValueId.generate(subject.toLowerCase().
                    replaceAll("\r", "").replaceAll("\t", " "));
            String subjectSID = SecondaryId.secondaryId(NameIds.SUBJECT, subjectVId);
            SecondaryId.createSecondaryId(db, jeName, subjectSID);
        }
    }
}
