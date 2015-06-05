package org.agilewiki.console.user;

import org.agilewiki.console.InitializeRoleTransaction;
import org.agilewiki.console.Role;
import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the user role.
 */
public class InitializeUserRoleTransaction extends InitializeRoleTransaction {
    public final static String NAME = "initializeUserRole";
    public static UserRole userRole;

    @Override
    public Role role() {
        return userRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);
    }
}
