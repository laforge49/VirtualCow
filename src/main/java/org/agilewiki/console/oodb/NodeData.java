package org.agilewiki.console.oodb;

import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.*;

public class NodeData {
    public final Db db;
    public final String nodeId;
    private NavigableMap<Comparable, List> atts = new TreeMap<Comparable, List>();

    public NodeData(Db db, String nodeId) {
        this.db = db;
        this.nodeId = nodeId;
        VersionedMapNode vmn = db.get(nodeId);
        if (vmn == null)
            atts = new TreeMap<>();
        else
            atts = vmn.flatMap(FactoryRegistry.MAX_TIMESTAMP);
    }

    public void clearMap() {
        atts.clear();
        db.clearMap(nodeId);
    }

    public void set(String key, Object value) {
        List l = atts.get(key);
        if (l == null) {
            l = new ArrayList();
            atts.put(key, l);
        }
        l.add(value);
        db.set(nodeId, key, value);
    }
}
