package org.agilewiki.vcow.roles.visitor.login;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;

public class BadUserPassword_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nbadUserPassword.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new BadUserPassword_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public BadUserPassword_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node instantiateNode(String nodeId, long timestamp) {
        return new BadUserPassword_Node(nodeId, timestamp);
    }
}
