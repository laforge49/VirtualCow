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

    public static void schedule(Db db, String itemId, String timeId) {
        if (isScheduled(db, itemId))
            throw new IllegalStateException("already scheduled");
        String scheduledId = scheduledId(itemId);
        String scheduleId = scheduleId(timeId);
        db.add(scheduledId, timeId);
        db.add(scheduleId, itemId);
    }

    public static void unschedule(Db db, String itemId) {
        String scheduledId = scheduledId(itemId);
        MapAccessor ma = db.mapAccessor();
        String scheduleId = (String) ma.get(scheduledId);
        if (scheduleId == null)
            return;
        db.remove(scheduledId, scheduleId);
        db.removeIfEmpty(scheduledId);
        db.remove(scheduleId, scheduledId);
        db.removeIfEmpty(scheduleId);
    }

    public static boolean isScheduled(Db db, String itemId) {
        String scheduledId = scheduledId(itemId);
        MapAccessor ma = db.mapAccessor();
        String scheduleId = (String) ma.get(scheduledId);
        return scheduleId != null;
    }
}
