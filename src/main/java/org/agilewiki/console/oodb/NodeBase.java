package org.agilewiki.console.oodb;

import org.agilewiki.utils.immutable.FactoryRegistry;

import java.util.Map;

/**
 * Base class for Node.
 */
public class NodeBase implements Node {
    private String nodeId;
    protected Map<String, String> parameters;
    private String factoryId;
    private long longTimestamp;

    public NodeBase(String nodeId, Map<String, String> parameters) {
        this(nodeId, parameters, null);
    }

    public NodeBase(String nodeId, Map<String, String> parameters, String factoryId) {
        this(nodeId, parameters, factoryId, FactoryRegistry.MAX_TIMESTAMP);
    }

    public NodeBase(String nodeId, Map<String, String> parameters, String factoryId, long longTimestamp) {
        this.nodeId = nodeId;
        this.parameters = parameters;
        this.factoryId = factoryId;
        this.longTimestamp = longTimestamp;
        getOODb().addNode(nodeId, this);
    }

    @Override
    public String getNodeId() {
        return null;
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
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
