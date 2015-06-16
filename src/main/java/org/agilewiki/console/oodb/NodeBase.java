package org.agilewiki.console.oodb;

import org.agilewiki.utils.immutable.FactoryRegistry;

import java.util.Map;

/**
 * Base class for Node.
 */
public class NodeBase implements Node {
    protected OODb ooDb;
    private String nodeId;
    protected Map<String, String> parameters;
    private String factoryId;
    private long longTimestamp;

    public NodeBase(OODb ooDb, String nodeId, Map<String, String> parameters) {
        this(ooDb, nodeId, parameters, null);
    }

    public NodeBase(OODb ooDb, String nodeId, Map<String, String> parameters, String factoryId) {
        this(ooDb, nodeId, parameters, factoryId, FactoryRegistry.MAX_TIMESTAMP);
    }

    public NodeBase(OODb ooDb, String nodeId, Map<String, String> parameters, String factoryId, long longTimestamp) {
        this.ooDb = ooDb;
        this.nodeId = nodeId;
        this.parameters = parameters;
        this.factoryId = factoryId;
        this.longTimestamp = longTimestamp;
        ooDb.addNode(nodeId, this);
    }

    @Override
    public OODb getOODb() {
        return ooDb;
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
