package org.agilewiki.console.roles.admin;

import org.agilewiki.console.RecreateRole_Node;
import org.agilewiki.console.roles.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class RecreateAdminRole_Node extends RecreateRole_Node {
    public final static String NAME = "recreateAdminRole";
    public static Admin_Role adminRole;

    public RecreateAdminRole_Node() {
    }

    public RecreateAdminRole_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Role role() {
        return adminRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);
    }
}
