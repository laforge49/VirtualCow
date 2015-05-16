package org.agilewiki.console.transactions;

import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

/**
 * Base class for all virtual cow transactions.
 */
public abstract class VCTransaction implements Transaction {
    @Override
    public final void transform(Db db, MapNode tMapNode) {
        process(db, tMapNode);
    }

    public abstract void process(Db db, MapNode tMapNode);
}
