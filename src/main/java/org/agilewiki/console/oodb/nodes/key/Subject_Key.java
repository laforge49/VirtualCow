package org.agilewiki.console.oodb.nodes.key;

public class Subject_Key extends Key_NodeInstance {
    private static Subject_Key subject_key;

    public static Subject_Key get() {
        return subject_key;
    }

    public static void create() {
        subject_key = new Subject_Key("$nsubject.key", "$nkey.node");
    }

    public Subject_Key(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
