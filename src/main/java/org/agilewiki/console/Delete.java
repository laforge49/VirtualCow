package org.agilewiki.console;

import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.Link2Id;
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
        for (String lnkTyp: Link1Id.link1LabelIdIterable(db, id)) {
            for (String tId: Link1Id.link1IdIterable(db, id, lnkTyp, db.getTimestamp())) {
                Link1Id.removeLink1(db, id, lnkTyp, tId);
            }
        }
        for (String lnkTyp: Link1Id.link1LabelInvIterable(db, id)) {
            for (String oId: Link1Id.link1InvIterable(db, id, lnkTyp, db.getTimestamp())) {
                Link1Id.removeLink1(db, oId, lnkTyp, id);
            }
        }
        for (String lnkTyp: Link2Id.link2LabelIdIterable(db, id)) {
            for (String tId: Link2Id.link2IdIterable(db, id, lnkTyp, db.getTimestamp())) {
                Link2Id.removeLink2(db, id, lnkTyp, tId);
            }
        }
    }
}
