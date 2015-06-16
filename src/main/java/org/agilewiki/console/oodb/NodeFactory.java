package org.agilewiki.console.oodb;

import java.util.Map;

/**
 * A factory to create a node.
 */
public interface NodeFactory {
    Node createNode(OODb ooDb, String nodeId, String factoryId, long longTimestamp);
}
