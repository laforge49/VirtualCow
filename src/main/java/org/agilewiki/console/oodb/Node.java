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

    void createSecondaryId(String keyId, String valueId);

    void removeSecondaryId(String keyId, String valueId);

    Iterable<String> nodeKeyIdIterable();

    String getNodeValue(String keyId);

    boolean nodeHasKeyId(String keyId);

    boolean nodeHasValueId(String keyId, String valueId);

    Iterable<String> nodeValueIdIterable(String keyId);

    void createLnk1(String labelId, String destinationNodeId);

    void removeLnk1(String labelId, String destinationNodeId);

    Iterable<String> label1IdIterable();

    boolean hasLabel1(String label1Id);

    boolean hasDestination(String label1Id, String destinationId);

    Iterable<String> destinationIdIterable(String label1Id);
}
