package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.RecreateRoleTransaction;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.Map;

/**
 * Creates the metadata for the admin role.
 */
public class RecreateAdminRoleTransaction extends RecreateRoleTransaction {
    public final static String NAME = "recreateAdminRole";
    public static AdminRole adminRole;

    public RecreateAdminRoleTransaction() {
    }

    public RecreateAdminRoleTransaction(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }

    @Override
    public Role role() {
        return adminRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);
    }
}
