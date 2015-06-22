package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class ServletStart_Node extends JournalEntry_Node {
    private static ServletStart_Node servletStart_node;
    public final static String ID = "$nservletStart.node";

    public static ServletStart_Node get() {
        return servletStart_node;
    }

    public static void create() {
        servletStart_node = new ServletStart_Node(ID, JournalEntry_Node.ID);
    }

    public ServletStart_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new ServletStart_NodeInstance(nodeId, factoryId);
    }
}
