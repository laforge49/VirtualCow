package org.agilewiki.console.oodb.nodes.roles.unRole.forgotPassword;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class ForgotPassword_Node extends JournalEntry_Node {
    private static ForgotPassword_Node forgotPassword_node;

    public static void create() {
        forgotPassword_node = new ForgotPassword_Node("$nforgotPassword.node", "$njournalEntry.node");
    }

    public ForgotPassword_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new ForgotPassword_NodeInstance(nodeId, factoryId);
    }
}
