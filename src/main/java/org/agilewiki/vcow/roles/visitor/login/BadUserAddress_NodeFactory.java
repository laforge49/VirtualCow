package org.agilewiki.vcow.roles.visitor.login;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;

public class BadUserAddress_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nbadUserAddress.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new BadUserAddress_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public BadUserAddress_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node instantiateNode(String nodeId, long timestamp) {
        return new BadUserAddress_Node(nodeId, timestamp);
    }
}
