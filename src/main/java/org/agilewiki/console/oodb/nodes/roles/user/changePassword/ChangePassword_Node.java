package org.agilewiki.console.oodb.nodes.roles.user.changePassword;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class ChangePassword_Node extends JournalEntry_Node {
    private static ChangePassword_Node changePassword_node;
    public final static String ID = "$nchangePassword.node";

    public static ChangePassword_Node get() {
        return changePassword_node;
    }

    public static void create() {
        changePassword_node = new ChangePassword_Node(ID);
    }

    public ChangePassword_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new ChangePassword_NodeInstance(nodeId);
    }
}
