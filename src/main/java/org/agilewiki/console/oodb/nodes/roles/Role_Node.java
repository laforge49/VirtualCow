package org.agilewiki.console.oodb.nodes.roles;

import org.agilewiki.console.oodb.NodeBase;
import org.agilewiki.console.oodb.nodes.Node_Node;

import java.util.Map;

/**
 * The root super class.
 */
public class Role_Node extends Node_Node {
    public Role_Node(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }
}
