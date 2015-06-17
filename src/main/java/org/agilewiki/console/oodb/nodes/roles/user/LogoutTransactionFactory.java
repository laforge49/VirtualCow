package org.agilewiki.console.oodb.nodes.roles.user;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;

import java.util.HashMap;

public class LogoutTransactionFactory implements NodeFactory {
    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new LogoutTransaction(nodeId, new HashMap<String, String>(), factoryId);
    }
}
