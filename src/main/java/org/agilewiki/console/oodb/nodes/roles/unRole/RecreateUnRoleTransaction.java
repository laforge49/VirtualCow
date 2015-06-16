package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.RecreateRoleTransaction;
import org.agilewiki.console.Role;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the unRole.
 */
public class RecreateUnRoleTransaction extends RecreateRoleTransaction {
    public final static String NAME = "recreateUnRole";
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
