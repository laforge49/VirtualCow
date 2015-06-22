package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class RecreateAdminRole_Node extends JournalEntry_Node {
    private static RecreateAdminRole_Node recreateAdminRole_Node;
    public final static String ID = "$nrecreateAdminRole.node";

    public static RecreateAdminRole_Node get() {
        return recreateAdminRole_Node;
    }

    public static void create() {
        recreateAdminRole_Node = new RecreateAdminRole_Node(ID, JournalEntry_Node.ID);
    }

    public RecreateAdminRole_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new RecreateAdminRole_NodeInstance(nodeId, factoryId);
    }
}
