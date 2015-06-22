package org.agilewiki.console.oodb.nodes.roles.user.logout;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class Logout_Node extends JournalEntry_Node {
    private static Logout_Node logout_node;
    public final static String ID = "$nlogout.node";

    public static Logout_Node get() {
        return logout_node;
    }

    public static void create() {
        logout_node = new Logout_Node(ID, JournalEntry_Node.ID);
    }

    public Logout_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Logout_NodeInstance(nodeId, factoryId);
    }
}
