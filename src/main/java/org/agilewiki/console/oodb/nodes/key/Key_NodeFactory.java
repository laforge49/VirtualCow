package org.agilewiki.console.oodb.nodes.key;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.MetaData_Node;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class Key_NodeFactory implements NodeFactory {
    public Key_NodeFactory(OODb ooDb) {
        ooDb.registerNodeFactory("key.node", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Key_Node(nodeId, new HashMap(), factoryId);
    }
}