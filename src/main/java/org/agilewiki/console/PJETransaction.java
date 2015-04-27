package org.agilewiki.console;

import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.*;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

import java.util.List;

/**
 * Proto-Journal Entries are stored transactions.
 */
public abstract class PJETransaction implements Transaction {
    public final static String PJE_ID = NameId.generate("pjeId");

    /**
     * Returns the name of the transaction.
     *
     * @return The transaction name.
     */
    public abstract String getName();

    public String createPJE(Db db) {
        String pjeId = RandomIds.randomId.generate();
        db.set(pjeId, Db.transactionNameId, getName());
        return pjeId;
    }

    public static String update(Db db, String pjeId) throws Exception {
        MapAccessor ma = db.mapAccessor();
        ListAccessor la = ma.listAccessor(pjeId);
        if (la == null)
            throw new IllegalArgumentException("missing pje: " + pjeId);
        VersionedMapNode pje = (VersionedMapNode) la.get(0);
        if (pje == null)
            throw new IllegalArgumentException("missing pje: " + pjeId);
        List names = pje.getList(Db.transactionNameId).flatList(FactoryRegistry.MAX_TIMESTAMP);
        if (names.isEmpty())
            throw new IllegalArgumentException("missing transaction name from pje: " + pjeId);
        String name = (String) names.get(0);
        MapNode mn = db.dbFactoryRegistry.nilMap;
        mn.set(PJE_ID, pjeId);
        MapAccessor pjeMA = pje.mapAccessor();
        for (ListAccessor pjeLA: pjeMA) {
            String key = pjeLA.key().toString();
            if (Db.transactionNameId.equals(key))
                continue;
            for (Object value: pjeLA) {
                mn.add(key, value);
            }
        }
        return db.update(name, mn).call();
    }
}
