package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.Role;

public class RecreateUnRole_NodeInstance extends RecreateRole_NodeInstance {
    public final static String NAME = "recreateUnRole";
    public static UnRole_Node unRole;

    public RecreateUnRole_NodeInstance() {
    }

    public RecreateUnRole_NodeInstance(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Role role() {
        return unRole;
    }
}
