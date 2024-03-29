package org.agilewiki.vcow.roles.developer.npje;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;

public class Npje_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nnpje.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new Npje_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public Npje_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node instantiateNode(String nodeId, long timestamp) {
        return new Npje_Node(nodeId, timestamp);
    }
}
