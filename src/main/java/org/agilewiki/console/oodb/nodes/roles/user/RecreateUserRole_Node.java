package org.agilewiki.console.oodb.nodes.roles.user;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class RecreateUserRole_Node extends JournalEntry_Node {
    public final static String ID = "$nrecreateUserRole.node";

    public static void create(OODb ooDb) {
        ooDb.addTimelessNode(new RecreateUserRole_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public RecreateUserRole_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new RecreateUserRole_NodeInstance(nodeId, timestamp);
    }
}
