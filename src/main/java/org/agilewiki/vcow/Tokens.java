package org.agilewiki.vcow;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Security tokens.
 */
public class Tokens {
    public static String generate(String identifier, long expTime)
            throws NoSuchAlgorithmException {
        String expirationTime = Long.toHexString(expTime);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String digest = VCUser_Node.bytesToHex(md.digest((identifier + expirationTime).getBytes()));
        return expirationTime + "|" + digest;
    }

    public static String parse(String idToken) {
        int i = idToken.indexOf("|");
        if (i == -1)
            return null;
        String userId = idToken.substring(0, i);
        String token = idToken.substring(i + 1);
        try {
            VCUser_Node user_node = (VCUser_Node) AwDb.getAwDb().fetchNode(userId, FactoryRegistry.MAX_TIMESTAMP);
            if (user_node == null)
                return null;
            if (validate(user_node.passwordDigest(), token)) {
                return userId;
            } else {
                return null;
            }
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }

    public static boolean validate(String identifier, String token)
            throws NoSuchAlgorithmException {
        int i = token.indexOf("|");
        if (i == -1)
            return false;
        String expirationTime = token.substring(0, i);
        long expTime = 0;
        try {
            expTime = Long.parseLong(expirationTime, 16);
        } catch (NumberFormatException nfe) {
            return false;
        }
        long time = System.currentTimeMillis();
        if (time > expTime) {
            return false;
        }
        String t = generate(identifier, expTime);
        return t.equals(token);
    }
}
