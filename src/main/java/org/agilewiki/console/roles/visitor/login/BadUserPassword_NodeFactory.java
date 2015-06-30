package org.agilewiki.console.roles.visitor.login;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class BadUserPassword_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nbadUserPassword.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new BadUserPassword_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public BadUserPassword_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new BadUserPassword_Node(nodeId, timestamp);
    }
}
