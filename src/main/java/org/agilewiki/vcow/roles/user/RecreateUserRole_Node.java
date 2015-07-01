package org.agilewiki.vcow.roles.user;

import org.agilewiki.vcow.RecreateRole_Node;
import org.agilewiki.vcow.roles.Role;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;

public class RecreateUserRole_Node extends RecreateRole_Node {
    public final static String NAME = "recreateUserRole";
    public static User_Role userRole;

    public RecreateUserRole_Node() {
    }

    public RecreateUserRole_Node(String nodeId, long timestamp) {
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
