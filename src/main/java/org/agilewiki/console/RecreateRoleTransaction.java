package org.agilewiki.console;

import org.agilewiki.console.admin.AdminRole;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the common metadata for a role.
 */
abstract public class RecreateRoleTransaction extends VCTransaction {
    abstract public Role role();

    @Override
    public void process(Db db, MapNode tMapNode) {
        String rolesId = NameId.generate("ROLES");
        String roleId = NameId.generate(role().roleName());
        String labelId = NameId.generate("roles");
        String linkId = Link1Id.link1Id(rolesId, labelId);
        VersionedMapNode vmn = db.get(linkId);
        String id = vmn == null ? null : (String) vmn.firstKey(db.getTimestamp());
        if (id != null) {
            Delete.delete(db, id);
        }
        Link1Id.createLink1(db, roleId, labelId, rolesId);
    }
}
