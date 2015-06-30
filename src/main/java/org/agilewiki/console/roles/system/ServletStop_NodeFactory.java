package org.agilewiki.console.roles.system;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.utils.immutable.FactoryRegistry;

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
        return new ServletStop_NodeInstance(nodeId, timestamp);
    }
}
