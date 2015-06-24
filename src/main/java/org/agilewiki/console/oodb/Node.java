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

    void clearMap();

    void set(String key, Object value);

    Object get(String key);

    List getFlatList(String key);

    NavigableMap<Comparable, List> getFlatMap();
}
