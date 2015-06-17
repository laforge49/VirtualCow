package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class User_NodeFactory implements NodeFactory {
    public User_NodeFactory(OODb ooDb) {
        ooDb.registerNodeFactory("user.node", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new MetaData_Node(nodeId, new HashMap(), factoryId);
    }
}
