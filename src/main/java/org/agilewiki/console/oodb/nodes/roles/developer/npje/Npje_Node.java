package org.agilewiki.console.oodb.nodes.roles.developer.npje;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class Npje_Node extends JournalEntry_Node {
    private static Npje_Node npje_node;

    public static Npje_Node get() {
        return npje_node;
    }

    public static void create() {
        npje_node = new Npje_Node("$nnpje.node", "$njournalEntry.node");
    }

    public Npje_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Npje_NodeInstance(nodeId, factoryId);
    }
}
