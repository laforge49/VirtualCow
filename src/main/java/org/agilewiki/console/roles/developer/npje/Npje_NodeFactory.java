package org.agilewiki.console.roles.developer.npje;

import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Npje_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nnpje.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new Npje_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public Npje_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Npje_Node(nodeId, timestamp);
    }
}
