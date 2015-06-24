package org.agilewiki.console.oodb;

import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class NodeData {
    public final Db db;
    public final String nodeId;
    private final NavigableMap<Comparable, List> atts;

    public NodeData(Db db, String nodeId) {
        this.db = db;
        this.nodeId = nodeId;
        VersionedMapNode vmn = db.get(nodeId);
        if (vmn == null)
            atts = new ConcurrentSkipListMap<>();
        else
            atts = vmn.flatMap(FactoryRegistry.MAX_TIMESTAMP);
    }

    public void clearMap() {
        db.clearMap(nodeId);
        atts.clear();
    }

    public void set(String key, Object value) {
        db.set(nodeId, key, value);
        List l = atts.get(key);
        if (l == null) {
            l = new ArrayList();
            atts.put(key, l);
        }
        l.add(value);
    }

    public Object get(String key) {
        return atts.get(key).get(0);
    }

    public List getFlatList(String key) {
        List l = atts.get(key);
        if (l == null)
            return new ArrayList();
        return new ArrayList(l);
    }
}
