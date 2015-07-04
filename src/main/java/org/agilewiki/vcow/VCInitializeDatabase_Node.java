package org.agilewiki.vcow;

import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.awdb.nodes.InitializeDatabase_Node;

public class VCInitializeDatabase_Node extends InitializeDatabase_Node {

    public VCInitializeDatabase_Node() {
    }

    public VCInitializeDatabase_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);
        System.out.println("Hi!");
    }
}
