package org.agilewiki.console.roles.system;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class RecreateSystemRole_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nrecreateSystemRole.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new RecreateSystemRole_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public RecreateSystemRole_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new RecreateSystemRole_Node(nodeId, timestamp);
    }
}
