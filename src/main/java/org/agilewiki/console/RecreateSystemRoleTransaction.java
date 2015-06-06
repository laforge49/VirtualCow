package org.agilewiki.console;

import org.agilewiki.console.unRole.UnRole;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
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
        String rolesId = NameId.generate("systemRole");
        String labelId = NameId.generate("link1s");
        Link1Id.createLink1(db, "$nuser", labelId, rolesId);
        SecondaryId.createSecondaryId(db, "$nuser", SecondaryId.secondaryId("$nlink1Links", "$nuserLink"));
        Link1Id.createLink1(db, "$nroles", labelId, rolesId);
        SecondaryId.createSecondaryId(db, "$nroles", SecondaryId.secondaryId("$nlink1Links", "$nrolesLink"));
        Link1Id.createLink1(db, "$nlink1s", labelId, rolesId);
        SecondaryId.createSecondaryId(db, "$nlink1s", SecondaryId.secondaryId("$nlink1Links", "$nlink1sLink"));
        SecondaryId.createSecondaryId(db, "$nlink1s", SecondaryId.secondaryId("$ninvDependent", "$nlink1Links"));
    }
}
