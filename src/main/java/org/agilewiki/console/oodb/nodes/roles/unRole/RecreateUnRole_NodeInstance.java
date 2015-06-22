package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.console.oodb.nodes.roles.unRole.login.BadUserAddress_Node;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

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

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);

        //JournalEntry_Node.define("$nbadUserAddress.node", UnRole_Node.ID);
        //JournalEntry_Node.define("$nbadUserPassword.node", UnRole_Node.ID);
    }
}
