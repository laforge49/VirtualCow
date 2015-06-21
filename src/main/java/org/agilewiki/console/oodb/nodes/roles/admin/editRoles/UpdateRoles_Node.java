package org.agilewiki.console.oodb.nodes.roles.admin.editRoles;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class UpdateRoles_Node extends JournalEntry_Node {
    private static UpdateRoles_Node updateRolesNode;

    public static UpdateRoles_Node get() {
        return updateRolesNode;
    }

    public static void create() {
        updateRolesNode = new UpdateRoles_Node("$nupdateRoles.node", "$njournalEntry.node");
    }

    public UpdateRoles_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new UpdateRoles_NodeInstance(nodeId, factoryId);
    }
}
