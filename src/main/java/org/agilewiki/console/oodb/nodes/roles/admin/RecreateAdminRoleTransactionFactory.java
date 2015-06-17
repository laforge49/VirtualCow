package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;

import java.util.HashMap;

public class RecreateAdminRoleTransactionFactory implements NodeFactory {
    @Override
    public Node createNode(String nodeId, String factoryId, long longTimestamp) {
        return new RecreateAdminRoleTransaction(nodeId, new HashMap<String, String>(), factoryId, longTimestamp);
    }
}
