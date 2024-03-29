package org.agilewiki.vcow.roles.visitor.forgotPassword;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;

public class ForgotPassword_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nforgotPassword.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new ForgotPassword_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public ForgotPassword_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node instantiateNode(String nodeId, long timestamp) {
        return new ForgotPassword_Node(nodeId, timestamp);
    }
}
