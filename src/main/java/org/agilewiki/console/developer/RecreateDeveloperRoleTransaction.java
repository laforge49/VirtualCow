package org.agilewiki.console.developer;

import org.agilewiki.console.RecreateRoleTransaction;
import org.agilewiki.console.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the user role.
 */
public class RecreateDeveloperRoleTransaction extends RecreateRoleTransaction {
    public final static String NAME = "recreateDeveloperRole";
    public static DeveloperRole developerRole;

    @Override
    public Role role() {
        return developerRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);
    }
}