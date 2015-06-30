package org.agilewiki.console.roles.visitor.forgotPassword;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class ForgotPassword_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nforgotPassword.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new ForgotPassword_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public ForgotPassword_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new ForgotPassword_NodeInstance(nodeId, timestamp);
    }
}
