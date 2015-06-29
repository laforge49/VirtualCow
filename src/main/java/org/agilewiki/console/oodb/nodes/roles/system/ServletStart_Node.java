package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class ServletStart_Node extends JournalEntry_Node {
    private static ServletStart_Node servletStart_node;
    public final static String ID = "$nservletStart.node";

    public static ServletStart_Node get() {
        return servletStart_node;
    }

    public static void create() {
        servletStart_node = new ServletStart_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public ServletStart_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new ServletStart_NodeInstance(nodeId, timestamp);
    }
}
