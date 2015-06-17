package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;

import java.util.HashMap;

public class RecreateDeveloperRoleTransactionFactory implements NodeFactory {
    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new RecreateDeveloperRoleTransaction(nodeId, new HashMap<String, String>(), factoryId);
    }
}