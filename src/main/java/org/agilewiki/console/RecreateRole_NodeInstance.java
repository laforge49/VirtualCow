package org.agilewiki.console;

import org.agilewiki.console.awdb.Delete;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.console.awdb.nodes.Key_Node;
import org.agilewiki.console.roles.Role;
import org.agilewiki.console.roles.Role_Node;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the common metadata for a role.
 */
abstract public class RecreateRole_NodeInstance extends JournalEntry_NodeInstance {

    public RecreateRole_NodeInstance() {
    }

    public RecreateRole_NodeInstance(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    abstract public Role role();

    @Override
    public void process(Db db, MapNode tMapNode) {
        final String thisRoleId = "$n" + role().roleName();
        final String thisRoleNodeId = thisRoleId + ".role";

        Delete.delete(thisRoleNodeId);

        getOoDb().createSecondaryId(thisRoleNodeId, Key_Node.NODETYPE_ID, Role_Node.ID);
    }
}
