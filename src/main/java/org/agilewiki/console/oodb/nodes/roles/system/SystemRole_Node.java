package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;
import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;

/**
 * System role represents the general infrastructure.
 */
public class SystemRole_Node extends Role_NodeInstance {
    private static SystemRole_Node systemRole_node;
    public final static String ID = "$nsystemRole.node";

    public static SystemRole_Node get() {
        return systemRole_node;
    }

    public static void create() {
        systemRole_node = new SystemRole_Node(ID, Role_Node.ID);
    }

    public SystemRole_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
        niceRoleName = "System";
        RecreateSystemRole_Node.create();
        simpleSimon.db.registerTransaction(RecreateSystemRole_NodeInstance.NAME, RecreateSystemRole_NodeInstance.class);
        RecreateSystemRole_NodeInstance.systemRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateSystemRole_NodeInstance.NAME;
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return null;
    }
}
