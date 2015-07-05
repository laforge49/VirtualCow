package org.agilewiki.vcow.roles.visitor.newUser;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.JournalEntry_NodeFactory;

public class NewUser_NodeFactory extends JournalEntry_NodeFactory {
    public final static String ID = "$nnewUser.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new NewUser_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public NewUser_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node instantiateNode(String nodeId, long timestamp) {
        return new NewUser_Node(nodeId, timestamp);
    }
}
