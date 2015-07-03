package org.agilewiki.vcow.roles.system;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;

public class ServletStop_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nservletStop.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new ServletStop_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public ServletStop_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new ServletStop_Node(nodeId, timestamp);
    }
}
