package org.agilewiki.vcow.roles.visitor;

import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.vcow.RecreateRole_Node;
import org.agilewiki.vcow.roles.Role;

public class RecreateVisitorRole_Node extends RecreateRole_Node {
    public final static String NAME = "recreateVisitorRole";
    public static Visitor_Role visitorRole;

    public RecreateVisitorRole_Node() {
    }

    public RecreateVisitorRole_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Role role() {
        return visitorRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);
    }
}
