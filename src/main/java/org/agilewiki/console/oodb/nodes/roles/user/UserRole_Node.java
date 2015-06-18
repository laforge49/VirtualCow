package org.agilewiki.console.oodb.nodes.roles.user;

import org.agilewiki.console.oodb.nodes.roles.Role_Node;

import java.util.Map;

/**
 * The root super class.
 */
public class UserRole_Node extends Role_Node {
    public UserRole_Node(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }
}
