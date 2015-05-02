package org.agilewiki.console;

import org.agilewiki.utils.virtualcow.Db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Security tokens.
 */
public class Tokens {
    public static String generate(Db db, String userId, long duration)
            throws NoSuchAlgorithmException {
        String expirationTime = User.bytesToHex(("" + System.currentTimeMillis() + duration).getBytes());
        String passwordDigest = User.passwordDigest(db, userId);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String digest = User.bytesToHex(md.digest((passwordDigest + expirationTime).getBytes()));
        return expirationTime + "|" + digest;
    }

    public static boolean validate(Db db, String userId, String token)
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
        if (time > expTime)
            return false;
        String t = generate(db, userId, expTime);
        return t.equals(token);
    }
}
