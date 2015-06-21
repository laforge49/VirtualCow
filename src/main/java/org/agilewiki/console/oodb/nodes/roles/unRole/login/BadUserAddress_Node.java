package org.agilewiki.console.oodb.nodes.roles.unRole.login;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class BadUserAddress_Node extends JournalEntry_Node {
    private static BadUserAddress_Node badUserAddress_node;

    public static void create() {
        badUserAddress_node = new BadUserAddress_Node("$nbadUserAddress.node", "$njournalEntry.node");
    }

    public BadUserAddress_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new BadUserAddress_NodeInstance(nodeId, factoryId);
    }
}
