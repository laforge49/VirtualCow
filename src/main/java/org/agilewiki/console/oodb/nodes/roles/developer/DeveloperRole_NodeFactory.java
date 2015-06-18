package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class DeveloperRole_NodeFactory implements NodeFactory {
    public DeveloperRole_NodeFactory(OODb ooDb) {
        ooDb.registerNodeFactory("developerRole.node", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new DeveloperRole_Node(nodeId, new HashMap(), factoryId);
    }
}
