package org.agilewiki.console.oodb.nodes.roles.admin.editRoles;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class UpdateRoles_Node extends JournalEntry_Node {
    public final static String ID = "$nupdateRoles.node";

    public static void create(OODb ooDb) {
        ooDb.addImmutableNode(new UpdateRoles_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public UpdateRoles_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new UpdateRoles_NodeInstance(nodeId, timestamp);
    }
}
