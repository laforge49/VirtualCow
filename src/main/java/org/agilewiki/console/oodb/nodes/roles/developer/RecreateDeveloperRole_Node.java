package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class RecreateDeveloperRole_Node extends JournalEntry_Node {
    private static RecreateDeveloperRole_Node recreateDeveloperRole_node;
    public final static String ID = "$nrecreateDeveloperRole.node";

    public static RecreateDeveloperRole_Node get() {
        return recreateDeveloperRole_node;
    }

    public static void create() {
        recreateDeveloperRole_node = new RecreateDeveloperRole_Node(ID, JournalEntry_Node.ID);
    }

    public RecreateDeveloperRole_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new RecreateDeveloperRole_NodeInstance(nodeId, factoryId);
    }
}
