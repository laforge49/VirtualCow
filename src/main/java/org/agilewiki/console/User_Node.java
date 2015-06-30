package org.agilewiki.console;

import org.agilewiki.console.awdb.Node;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.Node_NodeInstance;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class User_Node extends Node_NodeInstance {
    public final static String ID = "$nuser.node";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new User_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public User_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new User_NodeInstance(nodeId, timestamp);
    }
}
