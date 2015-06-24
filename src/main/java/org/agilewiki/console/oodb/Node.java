package org.agilewiki.console.oodb;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.utils.virtualcow.Db;

/**
 * A durable object.
 */
public interface Node {
    default OODb getOODb() {
        return SimpleSimon.simpleSimon.ooDb;
    }

    default Db getDb() {
        return getOODb().db;
    }

    String getNodeId();

    NodeData getNodeData();

    void setNodeData(NodeData nodeData);

    void delete();

    void endTransaction();
}
