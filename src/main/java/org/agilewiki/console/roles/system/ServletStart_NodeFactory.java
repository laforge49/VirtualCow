package org.agilewiki.console.roles.system;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class ServletStart_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nservletStart.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new ServletStart_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public ServletStart_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new ServletStart_NodeInstance(nodeId, timestamp);
    }
}
