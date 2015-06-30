package org.agilewiki.console.awdb.nodes;

import org.agilewiki.console.awdb.NodeBase;
import org.agilewiki.console.awdb.NodeFactory;

public abstract class Metadata_Node extends NodeBase implements NodeFactory {
    public Metadata_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }
}
