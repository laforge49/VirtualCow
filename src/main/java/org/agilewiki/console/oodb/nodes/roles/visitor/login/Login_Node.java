package org.agilewiki.console.oodb.nodes.roles.visitor.login;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Login_Node extends JournalEntry_Node {
    private static Login_Node login_node;
    public final static String ID = "$nlogin.node";

    public static Login_Node get() {
        return login_node;
    }

    public static void create() {
        login_node = new Login_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public Login_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Login_NodeInstance(nodeId, timestamp);
    }
}
