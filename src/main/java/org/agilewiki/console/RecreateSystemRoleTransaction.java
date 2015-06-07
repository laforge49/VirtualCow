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

        Link1Id.createLink1(db, "$nuser.link1", "$n.link1", "$nsystem.role");
        SecondaryId.createSecondaryId(db, "$nuser.link1", SecondaryId.secondaryId("$n.link1", "$nuser.link1"));
        Link1Id.createLink1(db, "$n.role.link1", "$n.link1", "$nsystem.role");
        SecondaryId.createSecondaryId(db, "$n.role.link1", SecondaryId.secondaryId("$n.link1", "$n.role.link1"));
        Link1Id.createLink1(db, "$n.link1.link1", "$n.link1", "$nsystem.role");
        SecondaryId.createSecondaryId(db, "$n.link1.link1", SecondaryId.secondaryId("$n.link1", "$n.link1.link1"));

        SecondaryId.createSecondaryId(db, "$n.link1.link1", SecondaryId.secondaryId("$ninvDependent", "$n.link1"));
    }
}
