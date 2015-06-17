package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;

import java.util.HashMap;

public class RecreateUnRoleTransactionFactory implements NodeFactory {
    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new RecreateUnRoleTransaction(nodeId, new HashMap<String, String>(), factoryId);
    }
}
