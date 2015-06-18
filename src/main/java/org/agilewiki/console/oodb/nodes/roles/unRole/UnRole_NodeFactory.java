package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class UnRole_NodeFactory implements NodeFactory {
    public UnRole_NodeFactory(OODb ooDb) {
        ooDb.registerNodeFactory("unRole.node", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new UnRole_Node(nodeId, new HashMap(), factoryId);
    }
}
