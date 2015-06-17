package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.NodeBase;

import java.util.Map;

/**
 * The root super class.
 */
public class MetaDataNode extends NodeBase {
    public MetaDataNode(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }
}
