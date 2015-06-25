package org.agilewiki.console.oodb;

import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;

public class NullNode implements Node {
    public final static NullNode singleton = new NullNode();

    private NullNode() {
    }

    @Override
    public String getNodeId() {
        return null;
    }

    @Override
    public NodeData getNodeData() {
        return null;
    }

    @Override
    public void setNodeData(NodeData nodeData) {

    }

    @Override
    public void endTransaction() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void clearMap() {

    }

    @Override
    public void set(String key, Object value) {

    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public List getFlatList(String key) {
        return null;
    }

    @Override
    public NavigableMap<Comparable, List> getFlatMap() {
        return null;
    }

    @Override
    public void createSecondaryId(String secondaryId) {

    }

    @Override
    public void removeSecondaryId(String secondaryId) {

    }

    @Override
    public Iterable<String> keyIdIteratable() {
        return null;
    }

    @Override
    public Iterable<String> keyValueIdIterable(String keyId) {
        return null;
    }

    @Override
    public Iterable<String> secondaryIdIterable(String keyId) {
        return null;
    }
}
