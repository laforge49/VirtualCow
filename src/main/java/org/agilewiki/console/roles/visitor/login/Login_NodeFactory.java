package org.agilewiki.console.roles.visitor.login;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Login_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nlogin.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new Login_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public Login_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Login_NodeInstance(nodeId, timestamp);
    }
}
