package org.agilewiki.console;

import org.agilewiki.console.awdb.Delete;
import org.agilewiki.console.awdb.nodes.JournalEntry_Node;
import org.agilewiki.console.awdb.nodes.Key_NodeFactory;
import org.agilewiki.console.roles.Role;
import org.agilewiki.console.roles.Role_NodeFactory;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the common metadata for a role.
 */
abstract public class RecreateRole_Node extends JournalEntry_Node {

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
