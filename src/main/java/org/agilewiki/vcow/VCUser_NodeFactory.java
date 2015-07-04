package org.agilewiki.vcow;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.Node_Node;
import org.agilewiki.awdb.nodes.User_NodeFactory;

public class VCUser_NodeFactory extends User_NodeFactory {

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new VCUser_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public VCUser_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new VCUser_Node(nodeId, timestamp);
    }
}
