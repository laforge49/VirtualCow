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
