package org.agilewiki.console.oodb;

/**
 * Base class for Node.
 */
public class NodeBase implements Node {
    protected String nodeId;

    public NodeBase(String nodeId) {
        this.nodeId = nodeId;
        if (nodeId != null)
            getOODb().addNode(nodeId, this);
    }

    @Override
    public String getNodeId() {
        return nodeId;
    }

    @Override
    public void endTransaction() {

    }
}
