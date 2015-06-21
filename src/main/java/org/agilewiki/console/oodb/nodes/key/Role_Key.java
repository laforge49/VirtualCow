package org.agilewiki.console.oodb.nodes.key;

public class Role_Key extends Key_NodeInstance {
    private static Role_Key role_key;

    public static void create() {
        role_key = new Role_Key("$nrole.key", "$nkey.node");
    }

    public Role_Key(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
