package org.agilewiki.console.transactions;

import org.agilewiki.console.TimeId;
import org.agilewiki.console.composite.ScheduleId;
import org.agilewiki.console.transactions.PJETransaction;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * A transaction scheduled for execution at a later time.
 */
public abstract class ScheduledTransaction extends PJETransaction {

    public String createPJE(Db db, long time, String userId) {
        String pjeId = super.createPJE(db, userId);
        String timeId = TimeId.generate(time);
        ScheduleId.schedule(db, pjeId, timeId);
        return pjeId;
    }

    @Override
    protected boolean ready(Db db, String pjeId) {
        return ScheduleId.isScheduled(db, pjeId);
    }

    @Override
    public void transform(Db db, MapNode mapNode) {
        String pjeId = (String) mapNode.get(PJE_ID);
        if (ready(db, pjeId))
            process(db, mapNode, pjeId);
    }

    protected void process(Db db, MapNode mapNode, String pjeId) {
        ScheduleId.unschedule(db, pjeId);
    }
}
