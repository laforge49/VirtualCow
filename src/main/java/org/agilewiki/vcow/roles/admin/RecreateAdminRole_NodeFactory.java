package org.agilewiki.vcow.roles.admin;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;

public class RecreateAdminRole_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nrecreateAdminRole.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new RecreateAdminRole_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public RecreateAdminRole_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new RecreateAdminRole_Node(nodeId, timestamp);
    }
}
