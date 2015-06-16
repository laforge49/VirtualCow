package org.agilewiki.console;

import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Run when the servlet has started.
 */
public class ServletStartTransaction extends VCTransaction {
    public final static String NAME = "servletStart";

    public static String update(Db db)
            throws Exception {
        MapNode mn = db.dbFactoryRegistry.nilMap;
        return db.update(NAME, mn).call();
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        User.init(SimpleSimon.simpleSimon);
    }
}
