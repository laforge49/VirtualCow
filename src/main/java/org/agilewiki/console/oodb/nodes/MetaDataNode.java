package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.NodeBase;
import org.agilewiki.console.oodb.OODb;

import java.util.Map;

/**
 * The root super class.
 */
public class MetaDataNode extends NodeBase {
    public MetaDataNode(OODb ooDb, String nodeId, Map<String, String> parameters, String factoryId, long longTimestamp) {
        super(ooDb, nodeId, parameters, factoryId, longTimestamp);
    }
}
