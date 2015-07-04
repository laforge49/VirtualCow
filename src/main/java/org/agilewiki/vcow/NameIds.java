package org.agilewiki.vcow;

import org.agilewiki.awdb.db.ids.NameId;

/**
 * NameId constants.
 */
public class NameIds extends NameId {
    public final static String BODY = generate("body");
    public final static String REMOTE_HOST = generate("remoteHost");
    public final static String REMOTE_ADDR = generate("remoteAddr");
    public final static String REMOTE_PORT = generate("remotePort");
    public final static String AN_ID = generate("anId");
    public final static String NODE_ID = generate("nodeId");
    public final static String REMOVEROLES_ID = generate("removeRoles");
    public final static String ADDROLES_ID = generate("addRoles");
    public static final String PASSWORD_KEY = NameId.generate("password");

    final public static String OFROLE_ID = "$nofRole";
    final public static String OFROLE_LNK1_ID = "$nofRole.lnk1";

    final public static String EMAIL_ID = "$nemail";
    final public static String EMAIL_KEY_ID = "$nemail.key";
    final public static String ROLE_ID = "$nrole";
    final public static String ROLE_KEY_ID = "$nrole.key";
    final public static String SUBJECT_ID = "$nsubject";
    final public static String SUBJECT_KEY_ID = "$nsubject.key";

    final public static String USER_ID = "$nuser";
    final public static String USER_LNK1_ID = "$nuser.lnk1";
}
