package org.agilewiki.console.oodb.nodes.roles.user.logout;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Logout_Node extends JournalEntry_Node {
    private static Logout_Node logout_node;
    public final static String ID = "$nlogout.node";

    public static Logout_Node get() {
        return logout_node;
    }

    public static void create() {
        logout_node = new Logout_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public Logout_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Logout_NodeInstance(nodeId, timestamp);
    }
}
