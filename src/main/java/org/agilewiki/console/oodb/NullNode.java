package org.agilewiki.console.oodb;

import java.util.List;

public class NullNode implements Node {
    public final static NullNode singleton = new NullNode();

    private NullNode() {}

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
}
