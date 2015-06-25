package org.agilewiki.console.oodb;

import java.util.List;
import java.util.NavigableMap;

/**
 * Base class for Node.
 */
public class NodeBase implements Node {
    private String nodeId;
    private NodeData innerReference = null;
    private NodeData outerReference = null;
    public final OODb ooDb;

    public NodeBase(String nodeId) {
        this.nodeId = nodeId;
        ooDb = getOODb();
        if (nodeId != null) {
            ooDb.addNode(nodeId, this);
            initialize();
        }
    }

    private void initialize() {
        innerReference = new NodeData(getDb(), nodeId);
        outerReference = innerReference;
    }

    @Override
    public String getNodeId() {
        return nodeId;
    }

    protected void setNodeId(String nodeId) {
        this.nodeId = nodeId;
        initialize();
    }

    public NodeData getNodeData() {
        if (getDb().isPrivileged())
            return innerReference;
        return outerReference;
    }

    @Override
    public void setNodeData(NodeData nodeData) {
        getDb().checkPrivilege();
        innerReference = nodeData;
    }

    @Override
    public void endTransaction() {
        outerReference = innerReference;
    }

    @Override
    public void clearMap() {
        innerReference.clearMap();
        ooDb.updated(this);
    }

    @Override
    public void set(String key, Object value) {
        innerReference.set(key, value);
        ooDb.updated(this);
    }

    @Override
    public void removeSecondaryId(String secondaryId) {
        innerReference.removeSecondaryId(secondaryId);
        ooDb.updated(this);
    }

    @Override
    public Object get(String key) {
        return getNodeData().get(key);
    }

    @Override
    public List getFlatList(String key) {
        return getNodeData().getFlatList(key);
    }

    @Override
    public NavigableMap<Comparable, List> getFlatMap() {
        return getNodeData().getFlatMap();
    }
}
