package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.oodb.nodes.roles.Role_Node;

import java.util.Map;

/**
 * The root super class.
 */
public class DeveloperRole_Node extends Role_Node {
    public DeveloperRole_Node(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }
}
