package org.agilewiki.vcow;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.nodes.Node_Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;

public class User_Node extends Node_Node {
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
