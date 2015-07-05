package org.agilewiki.vcow.roles.user.changePassword;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;

public class ChangePassword_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nchangePassword.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new ChangePassword_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public ChangePassword_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node instantiateNode(String nodeId, long timestamp) {
        return new ChangePassword_Node(nodeId, timestamp);
    }
}
