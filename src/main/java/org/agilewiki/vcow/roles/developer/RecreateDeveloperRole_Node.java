package org.agilewiki.vcow.roles.developer;

import org.agilewiki.vcow.RecreateRole_Node;
import org.agilewiki.vcow.roles.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class RecreateDeveloperRole_Node extends RecreateRole_Node {
    public final static String NAME = "recreateDeveloperRole";
    public static Developer_Role developerRole;

    public RecreateDeveloperRole_Node() {
    }

    public RecreateDeveloperRole_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Role role() {
        return developerRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);
    }
}
