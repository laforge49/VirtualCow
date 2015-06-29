package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class RecreateDeveloperRole_Node extends JournalEntry_Node {
    private static RecreateDeveloperRole_Node recreateDeveloperRole_node;
    public final static String ID = "$nrecreateDeveloperRole.node";

    public static RecreateDeveloperRole_Node get() {
        return recreateDeveloperRole_node;
    }

    public static void create() {
        recreateDeveloperRole_node = new RecreateDeveloperRole_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public RecreateDeveloperRole_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new RecreateDeveloperRole_NodeInstance(nodeId, timestamp);
    }
}
