package org.agilewiki.console.admin;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Updates the roles of a user.
 */
public class UpdateRolesTransaction extends VCTransaction {
    public final static String NAME = "updateRoles";

    @Override
    public void process(Db db, MapNode mapNode) {
    }
}
