package org.agilewiki.console.oodb.nodes.roles.user;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class RecreateUserRole_NodeInstance extends RecreateRole_NodeInstance {
    public final static String NAME = "recreateUserRole";
    public static UserRole_Node userRole;

    public RecreateUserRole_NodeInstance() {
    }

    public RecreateUserRole_NodeInstance(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Role role() {
        return userRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);
    }
}