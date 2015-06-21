package org.agilewiki.console.oodb.nodes.key;

public class InvDependency_Key extends Key_NodeInstance {
    private static InvDependency_Key invDependency_key;

    public static InvDependency_Key get() {
        return invDependency_key;
    }

    public static void create() {
        invDependency_key = new InvDependency_Key("$ninvDependency.key", "$nkey.node");
    }

    public InvDependency_Key(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
