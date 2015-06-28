package org.agilewiki.console;

import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.Key_Node;

import java.util.ArrayDeque;

/**
 * Delete.
 */
public class Delete {
    public static void delete(String id) {
        OODb ooDb = SimpleSimon.simpleSimon.ooDb;
        ArrayDeque<String> ids = new ArrayDeque<String>();
        deleter(ooDb, ids, id);
        while (!ids.isEmpty()) {
            id = ids.removeLast();
            ooDb.clearMap(id);
            for (String lnkTyp : ooDb.originLableIdIterable(id)) {
                for (String tId : ooDb.destinationIdIterable(id, lnkTyp, ooDb.getTimestamp())) {
                    ooDb.removeLnk1(id, lnkTyp, tId);
                }
            }
            for (String lnkTyp : ooDb.targetLabelInvIterable(id)) {
                for (String oId : ooDb.originIdIterable(id, lnkTyp, ooDb.getTimestamp())) {
                    ooDb.removeLnk1(oId, lnkTyp, id);
                }
            }
            for (String keyId : ooDb.keyIdIterable(id)) {
                for (String valueId : ooDb.keyValueIdIterable(id, keyId, ooDb.getTimestamp())) {
                    ooDb.removeSecondaryId(id, keyId, valueId);
                }
            }
        }
    }

    private static void deleter(OODb ooDb, ArrayDeque<String> ids, String id) {
        ids.addLast(id);
        for (String lnkTyp : ooDb.targetLabelInvIterable(id)) {
            for (String oId : ooDb.originIdIterable(id, lnkTyp, ooDb.getTimestamp())) {
                if (ooDb.hasKeyValue(lnkTyp + ".lnk1",
                        Key_Node.INVDEPENDENCY_ID,
                        lnkTyp,
                        ooDb.getTimestamp())) {
                    deleter(ooDb, ids, oId);
                }
            }
        }
    }
}
