package org.agilewiki.console.awdb.nodes;

import org.agilewiki.console.awdb.NodeBase;
import org.agilewiki.console.awdb.NodeFactory;

public abstract class Metadata_NodeInstance extends NodeBase implements NodeFactory {
    public Metadata_NodeInstance(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }
}
