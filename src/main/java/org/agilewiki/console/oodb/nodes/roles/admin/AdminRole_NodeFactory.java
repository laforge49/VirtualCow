package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class AdminRole_NodeFactory implements NodeFactory {
    public AdminRole_NodeFactory(OODb ooDb) {
        ooDb.registerNodeFactory("adminRole.node", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new AdminRole_Node(nodeId, new HashMap(), factoryId);
    }
}
