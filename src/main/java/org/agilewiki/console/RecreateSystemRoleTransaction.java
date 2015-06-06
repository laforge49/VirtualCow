package org.agilewiki.console;

import org.agilewiki.console.unRole.UnRole;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the unRole.
 */
public class RecreateSystemRoleTransaction extends RecreateRoleTransaction {
    public final static String NAME = "recreateSystemRole";
    public static SystemRole systemRole;

    @Override
    public Role role() {
        return systemRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);
    }
}
