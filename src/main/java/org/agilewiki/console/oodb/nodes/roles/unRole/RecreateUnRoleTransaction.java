package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.RecreateRoleTransaction;
import org.agilewiki.console.oodb.nodes.roles.Role;

import java.util.Map;

/**
 * Creates the metadata for the unRole.
 */
public class RecreateUnRoleTransaction extends RecreateRoleTransaction {
    public final static String NAME = "recreateUnRole";
    public static UnRole unRole;

    public RecreateUnRoleTransaction() {
    }

    public RecreateUnRoleTransaction(String nodeId, Map<String, String> parameters, String factoryId, long longTimestamp) {
        super(nodeId, parameters, factoryId, longTimestamp);
    }

    @Override
    public Role role() {
        return unRole;
    }
}
