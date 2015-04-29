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
public class ServletStartTransaction implements Transaction {
    public final static String NAME = "servletStart";

    public static ServletConfig servletConfig;

    public static String update(Db db)
            throws Exception {
        MapNode mn = db.dbFactoryRegistry.nilMap;
        return db.update(NAME, mn).call();
    }

    @Override
    public void transform(Db db, MapNode tMapNode) {
        ServletContext servletContext = servletConfig.getServletContext();
        User.init(db, servletConfig);
    }
}
