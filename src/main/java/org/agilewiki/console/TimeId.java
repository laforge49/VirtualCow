package org.agilewiki.console;

/**
 * Time Ids.
 */
public class TimeId {
    public static final String PREFIX = "$u";

    public static String generate(long time) {
        return PREFIX + Long.toHexString(time);
    }

    public static void validate(String timeId) {
        if (!timeId.startsWith(PREFIX))
            throw new IllegalArgumentException("not a time id: " + timeId);
    }

    public static long value(String timeId) {
        validate(timeId);
        return Long.parseUnsignedLong(timeId.substring(2), 16);
    }
}
