package org.agilewiki.console.admin;

import org.agilewiki.console.Delete;
import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the user role.
 */
public class InitializeAdminRoleTransaction extends VCTransaction {
    public final static String NAME = "initializeAdminRole";
    public static AdminRole adminRole;

    @Override
    public void process(Db db, MapNode tMapNode) {
        String rolesId = NameId.generate("ROLES");
        String adminId = NameId.generate("adminRole");
        String secondaryId = SecondaryId.secondaryId(rolesId, adminId);
        VersionedMapNode vmn = db.get(secondaryId);
        String id = vmn == null ? null : (String) vmn.firstKey(db.getTimestamp());
        if (id != null) {
            System.out.println("deleting");
            Delete.delete(db, id);
        }
        System.out.println("building");
        SecondaryId.createSecondaryId(db, adminId, secondaryId);
        System.out.println("done");
    }
}
