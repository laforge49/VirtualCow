package org.agilewiki.vcow.roles.system;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;

public class ServletStart_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nservletStart.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new ServletStart_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public ServletStart_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node instantiateNode(String nodeId, long timestamp) {
        return new ServletStart_Node(nodeId, timestamp);
    }
}
