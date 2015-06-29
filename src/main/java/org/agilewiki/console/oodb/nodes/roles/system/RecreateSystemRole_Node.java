package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class RecreateSystemRole_Node extends JournalEntry_Node {
    private static RecreateSystemRole_Node recreateSystemRole_node;
    public final static String ID = "$nrecreateSystemRole.node";

    public static RecreateSystemRole_Node get() {
        return recreateSystemRole_node;
    }

    public static void create() {
        recreateSystemRole_node = new RecreateSystemRole_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public RecreateSystemRole_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new RecreateSystemRole_NodeInstance(nodeId, timestamp);
    }
}
