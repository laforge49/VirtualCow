package org.agilewiki.console.roles.visitor;

import org.agilewiki.console.RecreateRole_Node;
import org.agilewiki.console.roles.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

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
