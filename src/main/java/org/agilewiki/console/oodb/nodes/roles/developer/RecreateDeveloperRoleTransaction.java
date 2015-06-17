package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.RecreateRoleTransaction;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.Map;

/**
 * Creates the metadata for the user role.
 */
public class RecreateDeveloperRoleTransaction extends RecreateRoleTransaction {
    public final static String NAME = "recreateDeveloperRole";
    public static DeveloperRole developerRole;

    public RecreateDeveloperRoleTransaction() {
    }

    public RecreateDeveloperRoleTransaction(String nodeId, Map<String, String> parameters, String factoryId, long longTimestamp) {
        super(nodeId, parameters, factoryId, longTimestamp);
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
