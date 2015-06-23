package org.agilewiki.console;

import org.agilewiki.console.oodb.nodes.*;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the common metadata for a role.
 */
abstract public class RecreateRole_NodeInstance extends JournalEntry_NodeInstance {

    public RecreateRole_NodeInstance() {
    }

    public RecreateRole_NodeInstance(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    abstract public Role role();

    @Override
    public void process(Db db, MapNode tMapNode) {
        final String thisRoleId = "$n" + role().roleName();
        final String thisRoleNodeId = thisRoleId + ".role";

        Delete.delete(db, thisRoleNodeId);

        SecondaryId.createSecondaryId(db, thisRoleNodeId, SecondaryId.secondaryId(Key_Node.NODETYPE_ID, Role_Node.ID));
    }
}
