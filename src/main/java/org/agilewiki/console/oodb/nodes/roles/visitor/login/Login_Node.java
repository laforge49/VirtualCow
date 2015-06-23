package org.agilewiki.console.oodb.nodes.roles.visitor.login;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class Login_Node extends JournalEntry_Node {
    private static Login_Node login_node;
    public final static String ID = "$nlogin.node";

    public static Login_Node get() {
        return login_node;
    }

    public static void create() {
        login_node = new Login_Node(ID);
    }

    public Login_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new Login_NodeInstance(nodeId);
    }
}
