package org.agilewiki.vcow.roles.developer;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;

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