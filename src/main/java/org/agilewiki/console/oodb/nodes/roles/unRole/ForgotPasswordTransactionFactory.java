package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;

import java.util.HashMap;

public class ForgotPasswordTransactionFactory implements NodeFactory {
    @Override
    public Node createNode(String nodeId, String factoryId, long longTimestamp) {
        return new ForgotPasswordTransaction(nodeId, new HashMap<String, String>(), factoryId, longTimestamp);
    }
}
