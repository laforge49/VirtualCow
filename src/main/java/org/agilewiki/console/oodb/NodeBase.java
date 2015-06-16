package org.agilewiki.console.oodb;

/**
 * Base class for Node.
 */
public class NodeBase implements Node {
    private String nodeId;
    private String factoryId;
    private long longTimestamp;

    public NodeBase(String nodeId, String factoryId, long longTimestamp) {
        this.nodeId = nodeId;
        this.factoryId = factoryId;
        this.longTimestamp = longTimestamp;
    }

    @Override
    public String getNodeId() {
        return null;
    }

    @Override
    public String getFactoryId() {
        return null;
    }

    @Override
    public long getLongTimestamp() {
        return 0;
    }
}
