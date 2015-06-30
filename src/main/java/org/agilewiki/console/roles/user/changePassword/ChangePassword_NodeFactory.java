package org.agilewiki.console.roles.user.changePassword;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class ChangePassword_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nchangePassword.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new ChangePassword_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public ChangePassword_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new ChangePassword_NodeInstance(nodeId, timestamp);
    }
}
