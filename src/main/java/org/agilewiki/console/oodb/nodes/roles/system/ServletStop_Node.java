package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class ServletStop_Node extends JournalEntry_Node {
    private static ServletStop_Node servletStop_node;
    public final static String ID = "$nservletStop.node";

    public static ServletStop_Node get() {
        return servletStop_node;
    }

    public static void create() {
        servletStop_node = new ServletStop_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public ServletStop_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new ServletStop_NodeInstance(nodeId, timestamp);
    }
}
