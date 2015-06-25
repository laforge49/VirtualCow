package org.agilewiki.console.oodb;

import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class NodeData {
    public final Db db;
    public final String nodeId;
    private ConcurrentSkipListMap<Comparable, List> atts;
    private ConcurrentSkipListMap<String, ConcurrentSkipListSet> keys;

    public NodeData(Db db, String nodeId) {
        this.db = db;
        this.nodeId = nodeId;

        VersionedMapNode avmn = db.get(nodeId);
        if (avmn == null)
            atts = new ConcurrentSkipListMap<>();
        else
            atts = new ConcurrentSkipListMap<>(avmn.flatMap(FactoryRegistry.MAX_TIMESTAMP));

        keys = new ConcurrentSkipListMap<>();
        for (String keyId : SecondaryId.typeIdIterable(db, nodeId)) {
            String secondaryInv = SecondaryId.secondaryInv(nodeId, keyId);
            for (String valueId : db.keysIterable(secondaryInv, FactoryRegistry.MAX_TIMESTAMP)) {
                if (SecondaryId.hasSecondaryId(db, nodeId, keyId, valueId, FactoryRegistry.MAX_TIMESTAMP)) {
                    ConcurrentSkipListSet values = keys.get(keyId);
                    if (values == null) {
                        values = new ConcurrentSkipListSet<>();
                        keys.put(keyId, values);
                    }
                    values.add(valueId);
                }
            }
        }
    }

    public NodeData(NodeData old) {
        db = old.db;
        nodeId = old.nodeId;

        atts = new ConcurrentSkipListMap<>();
        for (Comparable attId : old.atts.keySet()) {
            List l = old.atts.get(attId);
            atts.put(attId, new ArrayList<>(l));
        }

        keys = new ConcurrentSkipListMap<>();
        for (String keyId : old.keys.keySet()) {
            ConcurrentSkipListSet s = old.keys.get(keyId);
            keys.put(keyId, new ConcurrentSkipListSet<>(s));
        }
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

    public NavigableMap<Comparable, List> getFlatMap() {
        return new TreeMap<>(atts);
    }

    public void createSecondaryId(String secondaryId) {
        SecondaryId.createSecondaryId(db, nodeId, secondaryId);
        String keyType = SecondaryId.secondaryIdType(secondaryId);
        ConcurrentSkipListSet s = keys.get(keyType);
        if (s == null) {
            s = new ConcurrentSkipListSet();
            keys.put(keyType, s);
        }
        String keyValue = SecondaryId.secondaryIdValue(secondaryId);
        s.add(keyValue);
    }

    public void removeSecondaryId(String secondaryId) {
        SecondaryId.removeSecondaryId(db, nodeId, secondaryId);
        String keyType = SecondaryId.secondaryIdType(secondaryId);
        ConcurrentSkipListSet s = keys.get(keyType);
        if (s == null)
            return;
        String keyValue = SecondaryId.secondaryIdValue(secondaryId);
        s.remove(keyValue);
    }

    Iterator<String> keyIdIterator() {
        return keys.keySet().iterator();
    }

    public Iterable<String> keyIdIteratable() {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return keyIdIterator();
            }
        };
    }

    Iterator<String> keyValueIdIterator(String keyId) {
        ConcurrentSkipListSet s = keys.get(keyId);
        if (s == null)
            s = new ConcurrentSkipListSet();
        return s.iterator();
    }

    public Iterable<String> keyValueIdIterable(String keyId) {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return keyValueIdIterator(keyId);
            }
        };
    }

    Iterator<String> secondaryIdIterator(String keyId) {
        Iterator<String> kvii = keyValueIdIterator(keyId);
        return new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return kvii.hasNext();
            }

            @Override
            public String next() {
                return SecondaryId.secondaryId(keyId, kvii.next());
            }
        };
    }

    public Iterable<String> secondaryIdIterable(String keyId) {
        return new Iterable<String>() {

            @Override
            public Iterator<String> iterator() {
                return secondaryIdIterator(keyId);
            }
        };
    }
}
