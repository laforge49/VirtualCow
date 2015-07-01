package org.agilewiki.vcow.roles.admin;

import org.agilewiki.vcow.RecreateRole_Node;
import org.agilewiki.vcow.roles.Role;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;

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
