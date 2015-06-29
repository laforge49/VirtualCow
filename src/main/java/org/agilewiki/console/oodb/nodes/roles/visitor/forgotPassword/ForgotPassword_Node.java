package org.agilewiki.console.oodb.nodes.roles.visitor.forgotPassword;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class ForgotPassword_Node extends JournalEntry_Node {
    private static ForgotPassword_Node forgotPassword_node;
    public final static String ID = "$nforgotPassword.node";

    public static ForgotPassword_Node get() {
        return forgotPassword_node;
    }

    public static void create() {
        forgotPassword_node = new ForgotPassword_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public ForgotPassword_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new ForgotPassword_NodeInstance(nodeId, timestamp);
    }
}
