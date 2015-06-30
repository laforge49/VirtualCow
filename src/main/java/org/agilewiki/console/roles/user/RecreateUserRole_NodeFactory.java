package org.agilewiki.console.roles.user;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class RecreateUserRole_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nrecreateUserRole.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new RecreateUserRole_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public RecreateUserRole_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new RecreateUserRole_Node(nodeId, timestamp);
    }
}
