package org.agilewiki.console.admin;

import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the user role.
 */
public class InitializeAdminRoleTransaction extends VCTransaction {
    public final static String NAME = "initializeAdminRole";
    public static AdminRole adminRole;

    @Override
    public void process(Db db, MapNode tMapNode) {

    }
}
