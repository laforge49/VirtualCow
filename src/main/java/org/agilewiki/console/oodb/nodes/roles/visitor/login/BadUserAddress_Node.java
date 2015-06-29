package org.agilewiki.console.oodb.nodes.roles.visitor.login;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class BadUserAddress_Node extends JournalEntry_Node {
    private static BadUserAddress_Node badUserAddress_node;
    public final static String ID = "$nbadUserAddress.node";

    public static BadUserAddress_Node get() {
        return badUserAddress_node;
    }

    public static void create() {
        badUserAddress_node = new BadUserAddress_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public BadUserAddress_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new BadUserAddress_NodeInstance(nodeId, timestamp);
    }
}
