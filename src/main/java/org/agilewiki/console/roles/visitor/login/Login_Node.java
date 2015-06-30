package org.agilewiki.console.roles.visitor.login;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Login_Node extends JournalEntry_Node {
    public final static String ID = "$nlogin.node";

    public static void create(OODb ooDb) {
        ooDb.addTimelessNode(new Login_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public Login_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Login_NodeInstance(nodeId, timestamp);
    }
}
