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
    public final static String NODE_ID = generate("nodeId");
    public final static String REMOVEROLES_ID = generate("removeRoles");
    public final static String ADDROLES_ID = generate("addRoles");
    public static final String PASSWORD_KEY = NameId.generate("password");

    public final static String USERS_SYSTEM_DOMAIN_ID = "$nusersSystem.domain";
    public final static String ADMINS_USER_DOMAIN_ID = "$nadminsUser.domain";

    final public static String EMAIL_ID = "$nemail";
    final public static String EMAIL_KEY_ID = "$nemail.key";
    final public static String ROLE_ID = "$nrole";
    final public static String ROLE_KEY_ID = "$nrole.key";
}
