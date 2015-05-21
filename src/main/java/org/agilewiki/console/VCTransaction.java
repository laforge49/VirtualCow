package org.agilewiki.console;

import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

/**
 * Base class for all virtual cow transactions.
 */
public abstract class VCTransaction implements Transaction {
    @Override
    public final void transform(Db db, MapNode tMapNode) {
        String userId = (String) tMapNode.get(User.USER_KEY);
        if (userId != null) {
            String jeName = db.getJEName();
            Link1Id.createLink1(db, jeName, User.USER_KEY, userId);
        }
        process(db, tMapNode);
    }

    public abstract void process(Db db, MapNode tMapNode);
}
