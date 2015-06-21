package org.agilewiki.console.oodb.nodes.key;

public class SuperType_Key extends Key_NodeInstance {
    private static SuperType_Key superType_key;

    public static SuperType_Key get() {
        return superType_key;
    }

    public static void create() {
        superType_key = new SuperType_Key("$nsuperType.key", "$nkey.node");
    }

    public SuperType_Key(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
