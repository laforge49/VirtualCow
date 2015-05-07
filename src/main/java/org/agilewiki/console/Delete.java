package org.agilewiki.console;

import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Delete.
 */
public class Delete {
    public static void delete(Db db, String id) {
        db.clearMap(id);
        for (String secondaryType : SecondaryId.typeIdIterable(db, id)) {
            for (String secondaryId : SecondaryId.secondaryIdIterable(db, id, secondaryType, db.getTimestamp())) {
                SecondaryId.removeSecondaryId(db, id, secondaryId);
            }
        }
    }
}
