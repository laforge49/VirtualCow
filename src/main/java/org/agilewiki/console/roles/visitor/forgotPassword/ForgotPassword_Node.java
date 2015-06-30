package org.agilewiki.console.roles.visitor.forgotPassword;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class ForgotPassword_Node extends JournalEntry_Node {
    public final static String ID = "$nforgotPassword.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new ForgotPassword_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public ForgotPassword_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new ForgotPassword_NodeInstance(nodeId, timestamp);
    }
}
