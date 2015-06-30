package org.agilewiki.console;

import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.User_NodeInstance;
import org.agilewiki.utils.immutable.FactoryRegistry;

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
        String digest = User_NodeInstance.bytesToHex(md.digest((identifier + expirationTime).getBytes()));
        return expirationTime + "|" + digest;
    }

    public static String parse(String idToken) {
        int i = idToken.indexOf("|");
        if (i == -1)
            return null;
        String userId = idToken.substring(0, i);
        String token = idToken.substring(i + 1);
        try {
            User_NodeInstance user_nodeInstance = (User_NodeInstance) OODb.getOoDb().fetchNode(userId, FactoryRegistry.MAX_TIMESTAMP);
            if (user_nodeInstance == null)
                return null;
            if (validate(user_nodeInstance.passwordDigest(), token)) {
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
