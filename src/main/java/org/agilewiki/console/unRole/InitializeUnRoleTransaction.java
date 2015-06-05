package org.agilewiki.console.unRole;

import org.agilewiki.console.VCTransaction;
import org.agilewiki.console.developer.DeveloperRole;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the unRole.
 */
public class InitializeUnRoleTransaction extends VCTransaction {
    public final static String NAME = "initializeUnRole";
    public static UnRole developerunRole;

    @Override
    public void process(Db db, MapNode tMapNode) {

    }
}
