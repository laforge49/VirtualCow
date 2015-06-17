package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.roles.Role_NodeFactory;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class Node_NodeFactory implements NodeFactory {
    public Node_NodeFactory(OODb ooDb) {
        ooDb.registerNodeFactory("node.node", this);
        new JournalEntry_NodeFactory(ooDb);
        new Role_NodeFactory(ooDb);
        new User_NodeFactory(ooDb);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Node_Node(nodeId, new HashMap(), factoryId);
    }
}
