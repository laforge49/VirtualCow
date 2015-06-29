package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class User_Node extends Node_NodeInstance {
    private static User_Node user_node;
    public final static String ID = "$nuser.node";

    public static User_Node get() {
        return user_node;
    }

    public static void create() {
        user_node = new User_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public User_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new User_NodeInstance(nodeId, timestamp);
    }
}
