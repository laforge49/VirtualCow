package org.agilewiki.console.admin;

import org.agilewiki.console.RecreateRoleTransaction;
import org.agilewiki.console.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the admin role.
 */
public class RecreateAdminRoleTransaction extends RecreateRoleTransaction {
    public final static String NAME = "recreateAdminRole";
    public static AdminRole adminRole;

    @Override
    public Role role() {
        return adminRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);
    }
}
