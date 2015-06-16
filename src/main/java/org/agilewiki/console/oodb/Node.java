package org.agilewiki.console.oodb;

import org.agilewiki.utils.immutable.FactoryRegistry;

/**
 * A durable object.
 */
public interface Node {
    String getNodeId();

    String getFactoryId();

    long getLongTimestamp();

    default boolean isCurrent(long longTimestamp) {
        return longTimestamp == FactoryRegistry.MAX_TIMESTAMP;
    }

    default boolean isCurrent() {
        return isCurrent(getLongTimestamp());
    }
}
