package org.agilewiki.vcow.roles.system;

import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.awdb.AwDb;
import org.agilewiki.vcow.roles.Role_NodeInstance;
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
        RecreateSystemRole_NodeFactory.create(getAwDb());
        getAwDb().registerTransaction(RecreateSystemRole_Node.NAME, RecreateSystemRole_Node.class);
        RecreateSystemRole_Node.systemRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateSystemRole_Node.NAME;
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return null;
    }
}
