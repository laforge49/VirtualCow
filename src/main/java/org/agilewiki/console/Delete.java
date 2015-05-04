package org.agilewiki.console;

import org.agilewiki.utils.virtualcow.Db;

/**
 * Delete.
 */
public class Delete {
    public static void delete(Db db, String id) {
        db.clearMap(id);
    }
}
