package org.agilewiki.console.oodb.nodes.roles.visitor;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class RecreateVisitorRole_Node extends JournalEntry_Node {
    private static RecreateVisitorRole_Node recreateVisitorRole_node;
    public final static String ID = "$nrecreateVisitorRole.node";

    public static RecreateVisitorRole_Node get() {
        return recreateVisitorRole_node;
    }

    public static void create() {
        recreateVisitorRole_node = new RecreateVisitorRole_Node(ID);
    }

    public RecreateVisitorRole_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new RecreateVisitorRole_NodeInstance(nodeId);
    }
}
