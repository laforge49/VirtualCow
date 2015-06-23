package org.agilewiki.console.oodb.nodes.roles.user;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class RecreateUserRole_Node extends JournalEntry_Node {
    private static RecreateUserRole_Node recreateUserRole_node;
    public final static String ID = "$nrecreateUserRole.node";

    public static RecreateUserRole_Node get() {
        return recreateUserRole_node;
    }

    public static void create() {
        recreateUserRole_node = new RecreateUserRole_Node(ID);
    }

    public RecreateUserRole_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new RecreateUserRole_NodeInstance(nodeId);
    }
}
