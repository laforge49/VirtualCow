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
        recreateAdminRole_Node = new RecreateAdminRole_Node(ID);
    }

    public RecreateAdminRole_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new RecreateAdminRole_NodeInstance(nodeId);
    }
}
