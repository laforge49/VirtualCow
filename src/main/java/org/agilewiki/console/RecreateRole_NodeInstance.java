package org.agilewiki.console;

import org.agilewiki.console.oodb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.console.oodb.nodes.Key_Node;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the common metadata for a role.
 */
abstract public class RecreateRole_NodeInstance extends JournalEntry_NodeInstance {

    public RecreateRole_NodeInstance() {
    }

    public RecreateRole_NodeInstance(String nodeId) {
        super(nodeId);
    }

    abstract public Role role();

    @Override
    public void process(Db db, MapNode tMapNode) {
        final String thisRoleId = "$n" + role().roleName();
        final String thisRoleNodeId = thisRoleId + ".role";

        Delete.delete(thisRoleNodeId);

        ooDb.createSecondaryId(thisRoleNodeId, Key_Node.NODETYPE_ID, Role_Node.ID);
    }
}
