package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.nodes.roles.Role_Node;

import java.util.Map;

/**
 * The root super class.
 */
public class SystemRole_Node extends Role_Node {
    public SystemRole_Node(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }
}
