package org.agilewiki.vcow.roles.user.newEmailAddress;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;

public class NewEmailAddress_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nnewEmailAddress.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new NewEmailAddress_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public NewEmailAddress_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new NewEmailAddress_Node(nodeId, timestamp);
    }
}
