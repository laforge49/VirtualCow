package org.agilewiki.console.oodb;

import org.agilewiki.utils.immutable.FactoryRegistry;

/**
 * Base class for Node.
 */
public class NodeBase implements Node {
    protected OODb ooDb;
    private String nodeId;
    private String factoryId;
    private long longTimestamp;

    public NodeBase(OODb ooDb, String nodeId) {
        this(ooDb, nodeId, null);
        ooDb.addNode(nodeId, this);
    }

    public NodeBase(OODb ooDb, String nodeId, String factoryId) {
        this(ooDb, nodeId, factoryId, FactoryRegistry.MAX_TIMESTAMP);
    }

    public NodeBase(OODb ooDb, String nodeId, String factoryId, long longTimestamp) {
        this.ooDb = ooDb;
        this.nodeId = nodeId;
        this.factoryId = factoryId;
        this.longTimestamp = longTimestamp;
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
    public String getFactoryId() {
        return null;
    }

    @Override
    public long getLongTimestamp() {
        return 0;
    }
}
