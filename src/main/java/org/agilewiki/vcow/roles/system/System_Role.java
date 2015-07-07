package org.agilewiki.vcow.roles.system;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.roles.VCRole_Node;

/**
 * System role represents the general infrastructure.
 */
public class System_Role extends VCRole_Node {
    public final static String ID = "$nsystem.role";

    public static void create(AwDb awDb) {
        awDb.addTimelessNode(new System_Role(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public System_Role(String nodeId, long timestamp) {
        super(nodeId, timestamp);
        niceRoleName = "System";
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return null;
    }
}
