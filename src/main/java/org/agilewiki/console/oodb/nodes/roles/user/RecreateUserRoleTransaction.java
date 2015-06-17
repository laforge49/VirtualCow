package org.agilewiki.console.oodb.nodes.roles.user;

import org.agilewiki.console.RecreateRoleTransaction;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.Map;

/**
 * Creates the metadata for the user role.
 */
public class RecreateUserRoleTransaction extends RecreateRoleTransaction {
    public final static String NAME = "recreateUserRole";
    public static UserRole userRole;

    public RecreateUserRoleTransaction() {
    }

    public RecreateUserRoleTransaction(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }

    @Override
    public Role role() {
        return userRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);
    }
}
