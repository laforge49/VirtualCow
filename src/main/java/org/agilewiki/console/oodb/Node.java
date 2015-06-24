package org.agilewiki.console.oodb;

import org.agilewiki.console.SimpleSimon;

/**
 * A durable object.
 */
public interface Node {
    default OODb getOODb() {
        return SimpleSimon.simpleSimon.ooDb;
    }

    String getNodeId();

    void endTransaction();
}
