package org.agilewiki.console.oodb.nodes.roles.developer.npje;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Npje_Node extends JournalEntry_Node {
    private static Npje_Node npje_node;
    public final static String ID = "$nnpje.node";

    public static Npje_Node get() {
        return npje_node;
    }

    public static void create() {
        npje_node = new Npje_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public Npje_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Npje_NodeInstance(nodeId, timestamp);
    }
}
