package org.agilewiki.console.oodb.nodes.key;

public class NodeType_Key extends Key_NodeInstance {
    private static NodeType_Key nodeType_key;

    public static void create() {
        nodeType_key = new NodeType_Key("$nnodeType.key", "$nkey.node");
    }

    public NodeType_Key(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
