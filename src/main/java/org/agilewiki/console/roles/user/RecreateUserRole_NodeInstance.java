package org.agilewiki.console.roles.user;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.roles.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class RecreateUserRole_NodeInstance extends RecreateRole_NodeInstance {
    public final static String NAME = "recreateUserRole";
    public static User_Role userRole;

    public RecreateUserRole_NodeInstance() {
    }

    public RecreateUserRole_NodeInstance(String nodeId, long timestamp) {
        super(nodeId, timestamp);
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
