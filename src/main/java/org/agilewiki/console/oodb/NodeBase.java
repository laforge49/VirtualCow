package org.agilewiki.console.oodb;

/**
 * Base class for Node.
 */
public class NodeBase implements Node {
    protected String nodeId;
    protected String factoryId;

    public NodeBase(String nodeId) {
        this(nodeId, null);
    }

    public NodeBase(String nodeId, String factoryId) {
        this.nodeId = nodeId;
        this.factoryId = factoryId;
        if (nodeId != null)
            getOODb().addNode(nodeId, this);
    }

    @Override
    public String getNodeId() {
        return nodeId;
    }

    @Override
    public String getFactoryId() {
        return factoryId;
    }
}
