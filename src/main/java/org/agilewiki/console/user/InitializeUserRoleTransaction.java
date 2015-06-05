package org.agilewiki.console.user;

import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the user role.
 */
public class InitializeUserRoleTransaction extends VCTransaction {
    public final static String NAME = "initializeUserRole";
    public static UserRole userRole;

    @Override
    public void process(Db db, MapNode tMapNode) {

    }
}
