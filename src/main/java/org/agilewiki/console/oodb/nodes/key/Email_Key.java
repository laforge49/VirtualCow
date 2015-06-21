package org.agilewiki.console.oodb.nodes.key;

public class Email_Key extends Key_NodeInstance {
    private static Email_Key email_key;

    public static Email_Key get() {
        return email_key;
    }

    public static void create() {
        email_key = new Email_Key("$nemail.key", "$nkey.node");
    }

    public Email_Key(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
