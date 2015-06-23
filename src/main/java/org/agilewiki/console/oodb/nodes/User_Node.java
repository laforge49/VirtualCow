package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;

public class User_Node extends Node_NodeInstance {
    private static User_Node user_node;
    public final static String ID = "$nuser.node";

    public static User_Node get() {
        return user_node;
    }

    public static void create() {
        user_node = new User_Node(ID);
    }

    public User_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new User_NodeInstance(nodeId);
    }
}
