package org.agilewiki.console.oodb.nodes.key;

import org.agilewiki.console.oodb.nodes.MetaData_Node;

import java.util.Map;

/**
 * The root super class.
 */
public class Key_Node extends MetaData_Node {
    public Key_Node(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }
}
