package org.agilewiki.console.oodb.nodes.roles.unRole.login;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class BadUserPassword_Node extends JournalEntry_Node {
    private static BadUserPassword_Node badUserPassword_node;

    public static void create() {
        badUserPassword_node = new BadUserPassword_Node("$nbadUserPassword.node", "$njournalEntry.node");
    }

    public BadUserPassword_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new BadUserPassword_NodeInstance(nodeId, factoryId);
    }
}
