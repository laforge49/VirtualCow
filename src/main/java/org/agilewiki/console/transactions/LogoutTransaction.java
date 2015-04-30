package org.agilewiki.console.transactions;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.User;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

import javax.servlet.http.HttpServletRequest;

/**
 * Non-performing Journal Entry.
 */
public class LogoutTransaction implements Transaction {
    public final static String NAME = "logout";

    public String update(Db db, String subject, String userId)
            throws Exception {
        MapNode mn = db.dbFactoryRegistry.nilMap;
        if (subject.length() > 0)
            mn = mn.add(NameIds.SUBJECT, subject);
        mn = mn.add(User.USER_KEY, userId);
        return db.update(NAME, mn).call();
    }

    @Override
    public void transform(Db db, MapNode mapNode) {
        String jeName = db.getJEName();
        String subject = (String) mapNode.get(NameIds.SUBJECT);
        if (subject != null) {
            String subjectVId = ValueId.generate(subject.toLowerCase());
            String subjectSID = SecondaryId.secondaryId(NameIds.SUBJECT, subjectVId);
            SecondaryId.createSecondaryId(db, jeName, subjectSID);
        }
    }
}
