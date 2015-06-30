package org.agilewiki.console.roles.developer.npje;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Npje_Node extends JournalEntry_Node {
    public final static String ID = "$nnpje.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new Npje_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public Npje_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Npje_NodeInstance(nodeId, timestamp);
    }
}
