package org.agilewiki.console.oodb.nodes.roles.visitor;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class RecreateVisitorRole_Node extends JournalEntry_Node {
    private static RecreateVisitorRole_Node recreateVisitorRole_node;
    public final static String ID = "$nrecreateVisitorRole.node";

    public static RecreateVisitorRole_Node get() {
        return recreateVisitorRole_node;
    }

    public static void create() {
        recreateVisitorRole_node = new RecreateVisitorRole_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public RecreateVisitorRole_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new RecreateVisitorRole_NodeInstance(nodeId, timestamp);
    }
}
