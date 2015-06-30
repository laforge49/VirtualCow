package org.agilewiki.console.roles.visitor;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class RecreateVisitorRole_Node extends JournalEntry_Node {
    public final static String ID = "$nrecreateVisitorRole.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new RecreateVisitorRole_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public RecreateVisitorRole_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new RecreateVisitorRole_NodeInstance(nodeId, timestamp);
    }
}
