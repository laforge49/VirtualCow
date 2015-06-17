package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;

import java.util.HashMap;

public class ServletStopTransactionFactory implements NodeFactory {
    @Override
    public Node createNode(String nodeId, String factoryId, long longTimestamp) {
        return new ServletStopTransaction(nodeId, new HashMap<String, String>(), factoryId, longTimestamp);
    }
}
