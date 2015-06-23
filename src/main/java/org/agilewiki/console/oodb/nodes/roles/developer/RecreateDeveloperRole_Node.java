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
        recreateDeveloperRole_node = new RecreateDeveloperRole_Node(ID);
    }

    public RecreateDeveloperRole_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new RecreateDeveloperRole_NodeInstance(nodeId);
    }
}
