package org.agilewiki.console;

import org.agilewiki.console.admin.AdminRole;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the common metadata for a role.
 */
abstract public class InitializeRoleTransaction extends VCTransaction {
    abstract public Role role();

    @Override
    public void process(Db db, MapNode tMapNode) {
        String rolesId = NameId.generate("ROLES");
        String adminId = NameId.generate(role().roleName());
        String secondaryId = SecondaryId.secondaryId(rolesId, adminId);
        VersionedMapNode vmn = db.get(secondaryId);
        String id = vmn == null ? null : (String) vmn.firstKey(db.getTimestamp());
        if (id != null) {
            Delete.delete(db, id);
        }
        SecondaryId.createSecondaryId(db, adminId, secondaryId);
    }
}
