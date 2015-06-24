package org.agilewiki.console.oodb;

/**
 * Base class for Node.
 */
public class NodeBase implements Node {
    private String nodeId;
    private NodeData innerReference = null;
    private NodeData outerReference = null;
    private boolean deleted;

    public NodeBase(String nodeId) {
        this.nodeId = nodeId;
        if (nodeId != null) {
            getOODb().addNode(nodeId, this);
            initialize();
        }
    }

    private void initialize() {
        innerReference = new NodeData(nodeId);
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
    public void delete() {
        getDb().checkPrivilege();
        deleted = true;
    }

    @Override
    public void endTransaction() {
        if (deleted) {
            getOODb().dropNode(nodeId);
        }
        outerReference = innerReference;
    }
}
