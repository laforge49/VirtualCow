package org.agilewiki.console.oodb.nodes.key;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class NodeType_KeyFactory implements NodeFactory {
    public NodeType_KeyFactory(OODb ooDb) {
        ooDb.registerNodeFactory("nodeType.key", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new NodeType_Key(nodeId, new HashMap(), factoryId);
    }
}
