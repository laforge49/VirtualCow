package org.agilewiki.console;

import org.agilewiki.utils.ids.NameId;

/**
 * NameId constants.
 */
public class NameIds extends NameId {
    public final static String TRANSACTION_NAME = "$ntransactionName";
    public final static String SUBJECT = generate("subject");
    public final static String BODY = generate("body");
    public final static String REMOTE_HOST = generate("remoteHost");
    public final static String REMOTE_ADDR = generate("remoteAddr");
    public final static String REMOTE_PORT = generate("remotePort");
}
