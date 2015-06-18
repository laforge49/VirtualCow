package org.agilewiki.console.oodb.nodes.roles.user;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class UserRole_NodeFactory implements NodeFactory {
    public UserRole_NodeFactory(OODb ooDb) {
        ooDb.registerNodeFactory("userRole.node", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new UserRole_Node(nodeId, new HashMap(), factoryId);
    }
}
