package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;

/**
 * System role represents the general infrastructure.
 */
public class System_Role extends Role_NodeInstance {
    private static System_Role system_role;
    public final static String ID = "$nsystem.role";

    public static System_Role get() {
        return system_role;
    }

    public static void create() {
        system_role = new System_Role(ID);
    }

    public System_Role(String nodeId) {
        super(nodeId);
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
