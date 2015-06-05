package org.agilewiki.console.unRole;

import org.agilewiki.console.InitializeRoleTransaction;
import org.agilewiki.console.Role;
import org.agilewiki.console.VCTransaction;
import org.agilewiki.console.developer.DeveloperRole;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the unRole.
 */
public class InitializeUnRoleTransaction extends InitializeRoleTransaction {
    public final static String NAME = "initializeUnRole";
    public static UnRole unRole;

    @Override
    public Role role() {
        return unRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);
    }
}
