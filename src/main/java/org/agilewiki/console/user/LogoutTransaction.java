package org.agilewiki.console.user;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Non-performing Journal Entry.
 */
public class LogoutTransaction extends VCTransaction {
    public final static String NAME = "logout";

    @Override
    public void process(Db db, MapNode mapNode) {
        String jeName = db.getJEName();
        String subject = (String) mapNode.get(NameIds.SUBJECT);
        if (subject != null) {
            String subjectVId = ValueId.generate(subject.toLowerCase());
            String subjectSID = SecondaryId.secondaryId(NameIds.SUBJECT, subjectVId);
            SecondaryId.createSecondaryId(db, jeName, subjectSID);
        }
    }
}
