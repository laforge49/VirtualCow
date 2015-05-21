package org.agilewiki.console.profile;

import org.agilewiki.console.Delete;
import org.agilewiki.console.NameIds;
import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Delete a user.
 */
public class DeleteTransaction extends VCTransaction {
    public final static String NAME = "delete";

    @Override
    public void process(Db db, MapNode mapNode) {
        String id = (String) mapNode.get(NameIds.AN_ID);
        Delete.delete(db, id);
    }
}
