package org.agilewiki.console;

import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates over the non-empty ids.
 */
public class IdIterable implements Iterable<String>, Iterator<String> {
    final Db db;
    final String prefix;
    String next = null;
    final long timestamp;
    String last = null;

    public IdIterable(Db db, String prefix, String last, long timestamp) {
        this.db = db;
        this.prefix = prefix;
        this.last = last;
        if (last == null || last.length() == 0)
            this.last = prefix;
        this.timestamp = timestamp;
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (next != null) {
            return true;
        }
        while (true) {
            try {
                MapAccessor ma = db.mapAccessor();
                next = last;
                while (true) {
                    Comparable hk = ma.higherKey(next);
                    if (hk == null) {
                        return false;
                    }
                    next = hk.toString();
                    if (!next.startsWith(prefix)) {
                        return false;
                    }
                    ListAccessor la = ma.listAccessor(next);
                    if (la == null)
                        continue;
                    VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                    if (vmn.isEmpty(timestamp))
                        continue;
                    last = la.key().toString();
                    next = last;
                    return true;
                }
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    @Override
    public String next() {
        if (!hasNext())
            throw new NoSuchElementException();
        String rv = next;
        next = null;
        return rv;
    }
}
