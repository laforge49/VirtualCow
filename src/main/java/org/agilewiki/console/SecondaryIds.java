package org.agilewiki.console;

import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.List;

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
        for (String mnId : SecondaryId.secondaryIdIterable(db, nodeId, RecreateRoleTransaction.NODETYPE_ID, longTimestamp)) {
            if (nodeIsA(db, mnId, metaNodeId, longTimestamp))
                return true;
        }
        return false;
    }

    public static boolean isNode(Db db, String id, long longTimestamp) {
        MapAccessor ma = db.mapAccessor();
        String secInv = SecondaryId.secondaryInv(id, "$nnodeType");
        VersionedMapNode veln = (VersionedMapNode) ma.get(secInv);
        return veln != null && !veln.isEmpty(longTimestamp);
    }

    public static String nodeTypeId(Db db, String nodeId, long longTimestamp) {
        String metaNodeId = null;
        for (String mnId : SecondaryId.secondaryIdIterable(db, nodeId, RecreateRoleTransaction.NODETYPE_ID, longTimestamp)) {
            if (metaNodeId == null || nodeIsA(db, mnId, metaNodeId, longTimestamp))
                metaNodeId = mnId;
        }
        return metaNodeId;
    }

    public static String nodeTypeId(Db db, String nodeId, List<String> metaIds, long longTimestamp) {
        String metaNodeId = null;
        for (String mnId : metaIds) {
            if (nodeIsA(db, nodeId, mnId, longTimestamp)) {
                if (metaNodeId == null || nodeIsA(db, mnId, metaNodeId, longTimestamp))
                    metaNodeId = mnId;
            }
        }
        return metaNodeId;
    }
}
