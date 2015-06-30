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
    public final static String AN_ID = generate("anId");
    public final static String NODE_ID = generate("nodeId");
    public final static String REMOVEROLES_ID = generate("removeRoles");
    public final static String ADDROLES_ID = generate("addRoles");
    public static final String EMAIL_ID = NameId.generate("email");
    public static final String ROLE_ID = NameId.generate("role");
    public static final String PASSWORD_KEY = NameId.generate("password");
    public static final String USER_KEY = NameId.generate("user");
}
