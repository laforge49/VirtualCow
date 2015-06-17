package org.agilewiki.console.oodb.nodes.key;

import org.agilewiki.console.oodb.NodeBase;

import java.util.Map;

/**
 * The root super class.
 */
public class NodeType_Key extends NodeBase {
    public NodeType_Key(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }
}
