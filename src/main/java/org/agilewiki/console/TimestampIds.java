package org.agilewiki.console;

import org.agilewiki.utils.ids.Timestamp;

/**
 * Provides methods missing from Timestamp.
 */
public class TimestampIds extends Timestamp {

    /**
     * Generate an id by prefixing a string value with $t.
     *
     * @param value A string.
     * @return The string $t + value.
     */
    public static String generate(String value) {
        return PREFIX + value;
    }

    /**
     * Returns the value of a timestampId.
     *
     * @param timestampId The timestampId.
     * @return The timestamp.
     */
    public static String value(String timestampId) {
        validateId(timestampId);
        return timestampId.substring(2);
    }
}
