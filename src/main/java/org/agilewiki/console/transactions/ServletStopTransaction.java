package org.agilewiki.console.transactions;

import org.agilewiki.console.User;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * Run when the servlet has started.
 */
public class ServletStopTransaction implements Transaction {
    public final static String NAME = "servletStop";

    public static String update(Db db)
            throws Exception {
        MapNode mn = db.dbFactoryRegistry.nilMap;
        return db.update(NAME, mn).call();
    }

    @Override
    public void transform(Db db, MapNode tMapNode) {
    }
}
