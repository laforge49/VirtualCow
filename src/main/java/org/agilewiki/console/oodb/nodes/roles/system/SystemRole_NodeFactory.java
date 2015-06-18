package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class SystemRole_NodeFactory implements NodeFactory {
    public SystemRole_NodeFactory(OODb ooDb) {
        ooDb.registerNodeFactory("systemRole.node", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new SystemRole_Node(nodeId, new HashMap(), factoryId);
    }
}
