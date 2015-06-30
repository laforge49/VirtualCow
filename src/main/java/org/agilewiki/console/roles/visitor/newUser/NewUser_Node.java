package org.agilewiki.console.roles.visitor.newUser;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class NewUser_Node extends JournalEntry_Node {
    public final static String ID = "$nnewUser.node";

    public static void create(OODb ooDb) {
        ooDb.addTimelessNode(new NewUser_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public NewUser_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new NewUser_NodeInstance(nodeId, timestamp);
    }
}