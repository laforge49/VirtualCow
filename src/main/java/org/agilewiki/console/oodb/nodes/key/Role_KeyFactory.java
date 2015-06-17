package org.agilewiki.console.oodb.nodes.key;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class Role_KeyFactory implements NodeFactory {
    public Role_KeyFactory(OODb ooDb) {
        ooDb.registerNodeFactory("role.key", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Role_Key(nodeId, new HashMap(), factoryId);
    }
}
