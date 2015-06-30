package org.agilewiki.console.roles.developer;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.roles.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class RecreateDeveloperRole_NodeInstance extends RecreateRole_NodeInstance {
    public final static String NAME = "recreateDeveloperRole";
    public static Developer_Role developerRole;

    public RecreateDeveloperRole_NodeInstance() {
    }

    public RecreateDeveloperRole_NodeInstance(String nodeId, long timestamp) {
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
