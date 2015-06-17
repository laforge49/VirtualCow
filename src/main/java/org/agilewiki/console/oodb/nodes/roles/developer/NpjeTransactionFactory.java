package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.nodes.roles.unRole.BadUserAddressTransaction;

import java.util.HashMap;

public class NpjeTransactionFactory implements NodeFactory {
    @Override
    public Node createNode(String nodeId, String factoryId, long longTimestamp) {
        return new NpjeTransaction(nodeId, new HashMap<String, String>(), factoryId, longTimestamp);
    }
}
