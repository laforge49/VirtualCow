package org.agilewiki.console.oodb;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.utils.immutable.FactoryRegistry;

import java.util.Map;

/**
 * A durable object.
 */
public interface Node {
    default OODb getOODb() {
        return SimpleSimon.simpleSimon.ooDb;
    }

    String getNodeId();

    Map<String, String> getParameters();

    String getFactoryId();

    long getLongTimestamp();

    default boolean isCurrent(long longTimestamp) {
        return longTimestamp == FactoryRegistry.MAX_TIMESTAMP;
    }

    default boolean isCurrent() {
        return isCurrent(getLongTimestamp());
    }
}
