package org.agilewiki.console.transactions;

import org.agilewiki.console.Delete;
import org.agilewiki.console.NameIds;
import org.agilewiki.console.User;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

/**
 * Create a new user.
 */
public class DeleteTransaction implements Transaction {
    public final static String NAME = "delete";

    public String update(Db db,
                         String id,
                         String subject)
            throws Exception {
        MapNode mn = db.dbFactoryRegistry.nilMap;
        mn = mn.add(NameIds.AN_ID, id);
        if (subject.length() > 0)
            mn = mn.add(NameIds.SUBJECT, subject);
        return db.update(NAME, mn).call();
    }

    @Override
    public void transform(Db db, MapNode mapNode) {
        String id = (String) mapNode.get(NameIds.AN_ID);
        Delete.delete(db, id);
    }
}
