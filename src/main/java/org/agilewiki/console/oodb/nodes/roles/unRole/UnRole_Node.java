package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.oodb.nodes.roles.Role_Node;

import java.util.Map;

/**
 * The root super class.
 */
public class UnRole_Node extends Role_Node {
    public UnRole_Node(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }
}
