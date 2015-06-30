package org.agilewiki.console.roles.system;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.roles.Role_NodeInstance;
import org.agilewiki.utils.immutable.FactoryRegistry;

/**
 * System role represents the general infrastructure.
 */
public class System_Role extends Role_NodeInstance {
    public final static String ID = "$nsystem.role";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new System_Role(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public System_Role(String nodeId, long timestamp) {
        super(nodeId, timestamp);
        niceRoleName = "System";
        RecreateSystemRole_Node.create(getOoDb());
        getOoDb().registerTransaction(RecreateSystemRole_NodeInstance.NAME, RecreateSystemRole_NodeInstance.class);
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
