package org.agilewiki.console;

import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

import javax.servlet.http.HttpServletRequest;

/**
 * Non-performing Journal Entry.
 */
public class NpjeTransaction implements Transaction {
    public final static String NAME = "npje";

    public String update(Db db, String subject, String body, HttpServletRequest request)
            throws Exception {
        MapNode mn = db.dbFactoryRegistry.nilMap;
        mn.add(NameIds.SUBJECT, subject);
        mn.add(NameIds.BODY, body);
        mn.add(NameIds.REMOTE_HOST, request.getRemoteHost());
        mn.add(NameIds.REMOTE_ADDR, request.getRemoteAddr());
        mn.add(NameIds.REMOTE_PORT, request.getRemotePort());
        return db.update(NAME, mn).call();
    }

    @Override
    public void transform(Db db, MapNode mapNode) {

    }
}
