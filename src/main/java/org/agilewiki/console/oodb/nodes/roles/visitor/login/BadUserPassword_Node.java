package org.agilewiki.console.oodb.nodes.roles.visitor.login;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class BadUserPassword_Node extends JournalEntry_Node {
    private static BadUserPassword_Node badUserPassword_node;
    public final static String ID = "$nbadUserPassword.node";

    public static BadUserPassword_Node get() {
        return badUserPassword_node;
    }

    public static void create() {
        badUserPassword_node = new BadUserPassword_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public BadUserPassword_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new BadUserPassword_NodeInstance(nodeId, timestamp);
    }
}
