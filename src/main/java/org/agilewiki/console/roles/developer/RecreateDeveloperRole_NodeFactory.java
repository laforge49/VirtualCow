package org.agilewiki.console.roles.developer;

import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class RecreateDeveloperRole_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nrecreateDeveloperRole.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new RecreateDeveloperRole_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public RecreateDeveloperRole_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new RecreateDeveloperRole_Node(nodeId, timestamp);
    }
}
