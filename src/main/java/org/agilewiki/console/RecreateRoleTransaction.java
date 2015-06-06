package org.agilewiki.console;

import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.Link1Id;
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
        String roleId = NameId.generate(role().roleName() + "Role");
        Delete.delete(db, roleId);
        String rolesId = NameId.generate("metadata");
        String labelId = NameId.generate("roles");
        Link1Id.createLink1(db, roleId, labelId, rolesId);
    }
}
