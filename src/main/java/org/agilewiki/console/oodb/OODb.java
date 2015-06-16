package org.agilewiki.console.oodb;

import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.utils.immutable.BaseRegistry;
import org.agilewiki.utils.virtualcow.Db;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Object Oriented Database.
 */
public class OODb {
    public final Db db;

    public OODb()
            throws Exception {
        new Plant();
        Path dbPath = Paths.get("vcow.db");
        int maxRootBlockSize = 100000;
        db = new Db(new BaseRegistry(), dbPath, maxRootBlockSize);

        if (Files.exists(dbPath))
            db.open();
        else
            db.open(true);
    }
}
