package org.agilewiki.console.roles.visitor;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class RecreateVisitorRole_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nrecreateVisitorRole.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new RecreateVisitorRole_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public RecreateVisitorRole_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new RecreateVisitorRole_NodeInstance(nodeId, timestamp);
    }
}
