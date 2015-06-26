package org.agilewiki.console.oodb;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.utils.virtualcow.Db;

import java.util.List;
import java.util.NavigableMap;

/**
 * A durable object.
 */
public interface Node {
    default OODb getOODb() {
        return SimpleSimon.simpleSimon.ooDb;
    }

    default Db getDb() {
        return getOODb().db;
    }

    String getNodeId();

    NodeData getNodeData();

    void setNodeData(NodeData nodeData);

    void endTransaction();

    void reset();

    void clearMap();

    void set(String key, Object value);

    Object get(String key);

    List getFlatList(String key);

    NavigableMap<Comparable, List> getFlatMap();

    void createSecondaryId(String secondaryId);

    void removeSecondaryId(String secondaryId);

    Iterable<String> keyIdIteratable();

    String getKeyValue(String keyId);

    boolean hasKey(String keyId);

    boolean hasKeyValue(String keyId, String value);

    Iterable<String> keyValueIdIterable(String keyId);

    Iterable<String> secondaryIdIterable(String keyId);
}
