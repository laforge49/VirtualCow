package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.oodb.nodes.Node_Node;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;

import java.util.Map;

/**
 * The root super class.
 */
public class AdminRole_Node extends Role_Node {
    public AdminRole_Node(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }
}
