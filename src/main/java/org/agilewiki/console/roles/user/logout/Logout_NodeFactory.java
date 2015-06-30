package org.agilewiki.console.roles.user.logout;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Logout_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nlogout.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new Logout_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public Logout_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Logout_NodeInstance(nodeId, timestamp);
    }
}
