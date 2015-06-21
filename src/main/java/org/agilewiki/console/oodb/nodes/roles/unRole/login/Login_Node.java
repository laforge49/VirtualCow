package org.agilewiki.console.oodb.nodes.roles.unRole.login;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class Login_Node extends JournalEntry_Node {
    private static Login_Node login_node;

    public static void create() {
        login_node = new Login_Node("$nlogin.node", "$njournalEntry.node");
    }

    public Login_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Login_NodeInstance(nodeId, factoryId);
    }
}
