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
            for (String lnkTyp : Link1Id.link1LabelIdIterable(db, id)) {
                for (String tId : Link1Id.link1IdIterable(db, id, lnkTyp, db.getTimestamp())) {
                    ooDb.removeLnk1(id, lnkTyp, tId);
                }
            }
            for (String lnkTyp : Link1Id.link1LabelInvIterable(db, id)) {
                for (String oId : Link1Id.link1InvIterable(db, id, lnkTyp, db.getTimestamp())) {
                    ooDb.removeLnk1(oId, lnkTyp, id);
                }
            }
            for (String secondaryType : ooDb.keyIdIterable(id)) {
                for (String secondaryId : ooDb.secondaryIdIterable(id, secondaryType, db.getTimestamp())) {
                    ooDb.removeSecondaryId(id, secondaryId);
                }
            }
            for (String lnkTyp : Link2Id.link2LabelIdIterable(db, id)) {
                for (String tId : Link2Id.link2IdIterable(db, id, lnkTyp, db.getTimestamp())) {
                    Link2Id.removeLink2(db, id, lnkTyp, tId);
                }
            }
        }
    }

    private static void deleter(OODb ooDb, Db db, ArrayDeque<String> ids, String id) {
        ids.addLast(id);
        for (String lnkTyp : Link1Id.link1LabelInvIterable(db, id)) {
            for (String oId : Link1Id.link1InvIterable(db, id, lnkTyp, db.getTimestamp())) {
                if (ooDb.hasKeyValue(lnkTyp + ".lnk1",
                        Key_Node.INVDEPENDENCY_ID,
                        lnkTyp,
                        db.getTimestamp())) {
                    deleter(ooDb, db, ids, oId);
                }
            }
        }
    }
}
