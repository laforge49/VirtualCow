package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.RecreateSystemRoleTransaction;
import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;

import java.util.HashMap;

public class UpdateRolesTransactionFactory implements NodeFactory {
    @Override
    public Node createNode(String nodeId, String factoryId, long longTimestamp) {
        return new UpdateRolesTransaction(nodeId, new HashMap<String, String>(), factoryId, longTimestamp);
    }
}
