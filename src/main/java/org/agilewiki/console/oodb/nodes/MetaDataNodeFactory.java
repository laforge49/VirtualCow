package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class MetaDataNodeFactory implements NodeFactory {
    public MetaDataNodeFactory(OODb ooDb) {
        ooDb.registerNodeFactory("metadata.node", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new MetaDataNode(nodeId, new HashMap(), factoryId);
    }
}
