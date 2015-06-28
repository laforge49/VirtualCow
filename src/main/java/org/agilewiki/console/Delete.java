package org.agilewiki.console;

import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.Key_Node;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.Link2Id;
import org.agilewiki.utils.virtualcow.Db;

import java.util.ArrayDeque;

/**
 * Delete.
 */
public class Delete {
    public static void delete(Db db, String id) {
        OODb ooDb = SimpleSimon.simpleSimon.ooDb;
        ArrayDeque<String> ids = new ArrayDeque<String>();
        deleter(ooDb, db, ids, id);
        while (!ids.isEmpty()) {
            id = ids.removeLast();
            ooDb.clearMap(id);
            for (String lnkTyp : ooDb.label1IdIterable(id)) {
                for (String tId : ooDb.destinationIdIterable(id, lnkTyp, ooDb.getTimestamp())) {
                    ooDb.removeLnk1(id, lnkTyp, tId);
                }
            }
            for (String lnkTyp : Link1Id.link1LabelInvIterable(db, id)) {
                for (String oId : Link1Id.link1InvIterable(db, id, lnkTyp, ooDb.getTimestamp())) {
                    ooDb.removeLnk1(oId, lnkTyp, id);
                }
            }
            for (String keyId : ooDb.keyIdIterable(id)) {
                for (String valueId : ooDb.keyValueIdIterable(id, keyId, ooDb.getTimestamp())) {
                    ooDb.removeSecondaryId(id, keyId, valueId);
                }
            }
            for (String lnkTyp : Link2Id.link2LabelIdIterable(db, id)) {
                for (String tId : Link2Id.link2IdIterable(db, id, lnkTyp, ooDb.getTimestamp())) {
                    Link2Id.removeLink2(db, id, lnkTyp, tId);
                }
            }
        }
    }

    private static void deleter(OODb ooDb, Db db, ArrayDeque<String> ids, String id) {
        ids.addLast(id);
        for (String lnkTyp : Link1Id.link1LabelInvIterable(db, id)) {
            for (String oId : Link1Id.link1InvIterable(db, id, lnkTyp, ooDb.getTimestamp())) {
                if (ooDb.hasKeyValue(lnkTyp + ".lnk1",
                        Key_Node.INVDEPENDENCY_ID,
                        lnkTyp,
                        ooDb.getTimestamp())) {
                    deleter(ooDb, db, ids, oId);
                }
            }
        }
    }
}
