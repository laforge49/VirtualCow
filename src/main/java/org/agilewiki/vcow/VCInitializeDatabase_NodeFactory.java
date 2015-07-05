package org.agilewiki.vcow;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.nodes.InitializeDatabase_NodeFactory;

public class VCInitializeDatabase_NodeFactory extends InitializeDatabase_NodeFactory {

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new VCInitializeDatabase_NodeFactory(ID, FactoryRegistry.MAX_TIMESTAMP));
        awDb.registerTransaction(VCInitializeDatabase_Node.NAME, VCInitializeDatabase_Node.class);
    }

    public VCInitializeDatabase_NodeFactory(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node instantiateNode(String nodeId, long timestamp) {
        return new VCInitializeDatabase_Node(nodeId, timestamp);
    }
}
