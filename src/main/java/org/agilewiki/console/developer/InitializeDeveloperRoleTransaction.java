package org.agilewiki.console.developer;

import org.agilewiki.console.InitializeRoleTransaction;
import org.agilewiki.console.Role;
import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the user role.
 */
public class InitializeDeveloperRoleTransaction extends InitializeRoleTransaction {
    public final static String NAME = "initializeDeveloperRole";
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
