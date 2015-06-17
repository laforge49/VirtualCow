package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class MetaData_NodeFactory implements NodeFactory {
    public MetaData_NodeFactory(OODb ooDb) {
        ooDb.registerNodeFactory("metadata.node", this);
        new Node_NodeFactory(ooDb);
        new Lnk1_NodeFactory(ooDb);
        new Key_NodeFactory(ooDb);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new MetaData_Node(nodeId, new HashMap(), factoryId);
    }
}
