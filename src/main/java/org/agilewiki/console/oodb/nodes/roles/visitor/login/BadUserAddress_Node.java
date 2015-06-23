package org.agilewiki.console.oodb.nodes.roles.visitor.login;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class BadUserAddress_Node extends JournalEntry_Node {
    private static BadUserAddress_Node badUserAddress_node;
    public final static String ID = "$nbadUserAddress.node";

    public static BadUserAddress_Node get() {
        return badUserAddress_node;
    }

    public static void create() {
        badUserAddress_node = new BadUserAddress_Node(ID);
    }

    public BadUserAddress_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new BadUserAddress_NodeInstance(nodeId);
    }
}
