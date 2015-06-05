package org.agilewiki.console.admin;

import org.agilewiki.console.Delete;
import org.agilewiki.console.InitializeRoleTransaction;
import org.agilewiki.console.Role;
import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the admin role.
 */
public class InitializeAdminRoleTransaction extends InitializeRoleTransaction {
    public final static String NAME = "initializeAdminRole";
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
