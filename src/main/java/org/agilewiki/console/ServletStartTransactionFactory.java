package org.agilewiki.console;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;

import java.util.HashMap;

public class ServletStartTransactionFactory implements NodeFactory {
    @Override
    public Node createNode(String nodeId, String factoryId, long longTimestamp) {
        return new ServletStartTransaction(nodeId, new HashMap<String, String>(), factoryId, longTimestamp);
    }
}
