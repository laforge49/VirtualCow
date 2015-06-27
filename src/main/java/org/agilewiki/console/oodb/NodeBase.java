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
    public void reset() {
        innerReference = outerReference;
    }

    private void prep() {
        if (innerReference == outerReference) {
            innerReference = new NodeData(innerReference);
            ooDb.updated(this);
        }
    }

    @Override
    public void clearMap() {
        prep();
        innerReference.clearMap();
    }

    @Override
    public void set(String key, Object value) {
        prep();
        innerReference.set(key, value);
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

    @Override
    public void createSecondaryId(String secondaryId) {
        prep();
        innerReference.createSecondaryId(secondaryId);
    }

    @Override
    public void removeSecondaryId(String secondaryId) {
        prep();
        innerReference.removeSecondaryId(secondaryId);
    }

    @Override
    public Iterable<String> keyIdIterable() {
        return getNodeData().keyIdIterable();
    }

    @Override
    public String getKeyValue(String keyId) {
        return getNodeData().getKeyValue(keyId);
    }

    @Override
    public boolean hasKey(String keyId) {
        return getNodeData().hasKey(keyId);
    }

    @Override
    public boolean hasKeyValue(String keyId, String value) {
        return getNodeData().hasKeyValue(keyId, value);
    }

    @Override
    public Iterable<String> keyValueIdIterable(String keyId) {
        return getNodeData().keyValueIdIterable(keyId);
    }

    @Override
    public Iterable<String> secondaryIdIterable(String keyId) {
        return getNodeData().secondaryIdIterable(keyId);
    }

    @Override
    public void createLnk1(String labelId, String destinationNodeId) {
        prep();
        innerReference.createLnk1(labelId, destinationNodeId);
    }

    @Override
    public void removeLnk1(String labelId, String destinationNodeId) {
        prep();
        innerReference.removeLnk1(labelId, destinationNodeId);
    }
}
