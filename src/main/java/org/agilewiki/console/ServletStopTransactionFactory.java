package org.agilewiki.console;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.nodes.roles.developer.RecreateDeveloperRoleTransaction;

import java.util.HashMap;

public class ServletStopTransactionFactory implements NodeFactory {
    @Override
    public Node createNode(String nodeId, String factoryId, long longTimestamp) {
        return new ServletStopTransaction(nodeId, new HashMap<String, String>(), factoryId, longTimestamp);
    }
}
