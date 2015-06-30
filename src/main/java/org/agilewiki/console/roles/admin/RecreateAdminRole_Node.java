package org.agilewiki.console.roles.admin;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class RecreateAdminRole_Node extends JournalEntry_Node {
    public final static String ID = "$nrecreateAdminRole.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new RecreateAdminRole_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public RecreateAdminRole_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new RecreateAdminRole_NodeInstance(nodeId, timestamp);
    }
}
