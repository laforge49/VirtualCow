package org.agilewiki.console.roles.admin;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.roles.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class RecreateAdminRole_NodeInstance extends RecreateRole_NodeInstance {
    public final static String NAME = "recreateAdminRole";
    public static Admin_Role adminRole;

    public RecreateAdminRole_NodeInstance() {
    }

    public RecreateAdminRole_NodeInstance(String nodeId, long timestamp) {
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
