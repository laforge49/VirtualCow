package org.agilewiki.vcow.roles.admin.editRoles;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;

public class UpdateRoles_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nupdateRoles.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new UpdateRoles_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public UpdateRoles_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new UpdateRoles_Node(nodeId, timestamp);
    }
}
