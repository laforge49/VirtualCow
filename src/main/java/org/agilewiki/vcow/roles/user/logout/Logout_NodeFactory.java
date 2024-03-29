package org.agilewiki.vcow.roles.user.logout;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;

public class Logout_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nlogout.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new Logout_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public Logout_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node instantiateNode(String nodeId, long timestamp) {
        return new Logout_Node(nodeId, timestamp);
    }
}
