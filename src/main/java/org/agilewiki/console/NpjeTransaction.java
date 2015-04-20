package org.agilewiki.console;

import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Non-performing Journal Entry.
 */
public class NpjeTransaction implements Transaction {
    public final static String NAME = "npje";

    public String update(Db db, String subject, String body, HttpServletRequest request)
            throws Exception {
        MapNode mn = db.dbFactoryRegistry.nilMap;
        if (subject.length() > 0)
            mn = mn.add(NameIds.SUBJECT, subject);
        if (body.length() > 0)
            mn = mn.add(NameIds.BODY, body);
        mn = mn.add(NameIds.REMOTE_HOST, request.getRemoteHost());
        mn = mn.add(NameIds.REMOTE_ADDR, request.getRemoteAddr());
        mn = mn.add(NameIds.REMOTE_PORT, request.getRemotePort());
        return db.update(NAME, mn).call();
    }

    @Override
    public void transform(Db db, MapNode mapNode) {
        String jeName = db.getJEName();
        MapAccessor ma = db.mapAccessor();
        ListAccessor la = ma.listAccessor(jeName);
        VersionedMapNode vmn = (VersionedMapNode) la.get(0);
        long timestamp = FactoryRegistry.MAX_TIMESTAMP;
        List subjectList = vmn.getList(NameIds.SUBJECT).flatList(timestamp);
        if (subjectList.size() > 0) {
            String subject = subjectList.get(0).toString();
            String subjectVId = ValueId.generate(subject);
            String subjectSID = SecondaryId.secondaryId(NameIds.SUBJECT, subjectVId);
            SecondaryId.createSecondaryId(db, jeName, subjectSID);
        }
    }
}
