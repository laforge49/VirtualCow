package org.agilewiki.console;

import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Adds methods to SecondaryId.
 */
public class SecondaryIds extends SecondaryId {
    public static boolean hasSecondaryType(Db db, String vmlId, String typeId, long timestamp) {
        String inv = secondaryInv(vmlId, typeId);
        MapAccessor ma = db.mapAccessor();
        ListAccessor la = ma.listAccessor(inv);
        if (la == null)
            return false;
        VersionedMapNode invn = (VersionedMapNode) la.get(0);
        if (invn == null)
            return false;
        return !invn.isEmpty(timestamp);
    }

    public static boolean nodeIsA(Db db, String nodeId, String metaNodeId, long longTimestamp) {
        if (nodeId.equals(metaNodeId))
            return true;
            if (nodeIsA(db, mnId, metaNodeId, longTimestamp))
                return true;
        }
        return false;
    }
}
