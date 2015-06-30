package org.agilewiki.console.roles.user.logout;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Logout_Node extends JournalEntry_Node {
    public final static String ID = "$nlogout.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new Logout_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public Logout_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Logout_NodeInstance(nodeId, timestamp);
    }
}
