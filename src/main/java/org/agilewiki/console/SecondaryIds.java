package org.agilewiki.console;

import org.agilewiki.console.oodb.nodes.Key_Node;
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
        for (String mnId : SimpleSimon.simpleSimon.ooDb.secondaryIdIterable(nodeId, Key_Node.NODETYPE_ID, longTimestamp)) {
            if (nodeIsA(db, mnId, metaNodeId, longTimestamp))
                return true;
        }
        return false;
    }

    public static boolean isNode(Db db, String id, long longTimestamp) {
        char idtyp = id.charAt(1);
        if (!Character.isLowerCase(idtyp) || idtyp == 'v')
            return false;
        MapAccessor ma = db.mapAccessor();
        String secInv = SecondaryId.secondaryInv(id, Key_Node.NODETYPE_ID);
        VersionedMapNode veln = (VersionedMapNode) ma.get(secInv);
        return veln != null && !veln.isEmpty(longTimestamp);
    }

    public static String nodeTypeId(Db db, String nodeId, long longTimestamp) {
        return SimpleSimon.simpleSimon.ooDb.getKeyValue(nodeId, Key_Node.NODETYPE_ID, longTimestamp);
    }

    public static String superTypeId(Db db, String nodeId, long longTimestamp) {
        return SimpleSimon.simpleSimon.ooDb.getKeyValue(nodeId, Key_Node.SUPERTYPE_ID, longTimestamp);
    }

    public static String kindId(Db db, String nodeId, long longTimestamp) {
        String kind = superTypeId(db, nodeId, longTimestamp);
        if (kind == null)
            kind = nodeTypeId(db, nodeId, longTimestamp);
        return kind;
    }
}
