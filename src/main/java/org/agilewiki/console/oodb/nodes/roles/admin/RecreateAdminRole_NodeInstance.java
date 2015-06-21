package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class RecreateAdminRole_NodeInstance extends RecreateRole_NodeInstance {
    public final static String NAME = "recreateAdminRole";
    public static AdminRole_Node adminRole;

    public RecreateAdminRole_NodeInstance() {
    }

    public RecreateAdminRole_NodeInstance(String nodeId, String factoryId) {
        super(nodeId, factoryId);
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
