package org.agilewiki.console.composite;

import org.agilewiki.console.TimeId;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.immutable.collections.ListNode;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.virtualcow.Db;

/**
 * An unversioned queue of scheduled proto journal entries.
 */
public class ScheduleId {
    public static final String SCHEDULE_ID = "$K";
    public static final String SCHEDULED_ID = "$L";

    public static String scheduleId(String timeId) {
        TimeId.validate(timeId);
        return SCHEDULE_ID + timeId;
    }

    public static String scheduledId(String itemId) {
        NameId.validateAnId(itemId);
        return SCHEDULED_ID + itemId;
    }

    public static void schedule(Db db, String timeId, String itemId) {
        if (isScheduled(db, itemId))
            throw new IllegalStateException("already scheduled");
        String scheduleId = scheduleId(timeId);
        String scheduledId = scheduledId(itemId);
        ListNode listNode = db.create(scheduleId);
        listNode.add(itemId);
        listNode = db.create(scheduledId);
        listNode.add(timeId);
    }

    public static void unschedule(Db db, String timeId, String itemId) {
        if (!isScheduled(db, itemId))
            return;
        String scheduleId = scheduleId(timeId);
        String scheduledId = scheduledId(itemId);
        MapAccessor ma = db.mapAccessor();
        db.remove(scheduleId, scheduledId);
        db.removeIfEmpty(scheduleId);
        db.remove(scheduledId, scheduleId);
        db.removeIfEmpty(scheduledId);
    }

    public static boolean isScheduled(Db db, String itemId) {
        String scheduledId = scheduledId(itemId);
        MapAccessor ma = db.mapAccessor();
        ListNode ln = (ListNode) ma.get(scheduledId);
        return ln != null && !ln.isEmpty();
    }
}
