package org.agilewiki.console.roles.visitor.login;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class BadUserAddress_Node extends JournalEntry_Node {
    public final static String ID = "$nbadUserAddress.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new BadUserAddress_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public BadUserAddress_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new BadUserAddress_NodeInstance(nodeId, timestamp);
    }
}
