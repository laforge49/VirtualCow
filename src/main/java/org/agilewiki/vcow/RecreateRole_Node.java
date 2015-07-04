package org.agilewiki.vcow;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Delete;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.awdb.nodes.Attribute_NodeFactory;
import org.agilewiki.awdb.nodes.Key_NodeFactory;
import org.agilewiki.awdb.nodes.Lnk1_NodeFactory;
import org.agilewiki.awdb.nodes.Node_NodeFactory;
import org.agilewiki.vcow.roles.Role;
import org.agilewiki.vcow.roles.Role_NodeFactory;

/**
 * Creates the common metadata for a role.
 */
abstract public class RecreateRole_Node extends VCJournalEntry_Node {

    public RecreateRole_Node() {
    }

    public RecreateRole_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    abstract public Role role();

    @Override
    public void process(Db db, MapNode tMapNode) {
        final String thisRoleId = "$n" + role().roleName();
        final String thisRoleNodeId = thisRoleId + ".role";

        Delete.delete(thisRoleNodeId);

        getAwDb().createSecondaryId(thisRoleNodeId, Key_NodeFactory.NODETYPE_ID, Role_NodeFactory.ID);
    }
}
