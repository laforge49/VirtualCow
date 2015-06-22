package org.agilewiki.console.oodb.nodes.roles.unRole.login;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class BadUserPassword_Node extends JournalEntry_Node {
    private static BadUserPassword_Node badUserPassword_node;
    public final static String ID = "$nbadUserPassword.node";

    public static BadUserPassword_Node get() {
        return badUserPassword_node;
    }

    public static void create() {
        badUserPassword_node = new BadUserPassword_Node(ID, JournalEntry_Node.ID);
    }

    public BadUserPassword_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new BadUserPassword_NodeInstance(nodeId, factoryId);
    }
}
