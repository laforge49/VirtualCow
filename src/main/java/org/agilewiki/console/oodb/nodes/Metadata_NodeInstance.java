package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.NodeBase;
import org.agilewiki.console.oodb.NodeFactory;

public abstract class Metadata_NodeInstance extends NodeBase implements NodeFactory {
    public Metadata_NodeInstance(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
