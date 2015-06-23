package org.agilewiki.console.oodb;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.agilewiki.console.SecondaryIds;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.utils.immutable.BaseRegistry;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.virtualcow.Db;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Object Oriented Database, without state caching.
 */
public class OODb {
    public final Db db;
    private Map<String, Node> nodeCache;
    private Map<String, NodeFactory> nodeFactories = new ConcurrentHashMap<String, NodeFactory>(16, 0.75f, 1);

    public OODb(int maxRootBlockSize, long maxNodeCacheSize)
            throws Exception {
        new Plant();
        Path dbPath = Paths.get("vcow.db");
        db = new Db(new BaseRegistry(), dbPath, maxRootBlockSize);
        if (Files.exists(dbPath))
            db.open();
        else
            db.open(true);
        final LoadingCache<String, Node> cache = CacheBuilder.newBuilder().concurrencyLevel(1).
                softValues().build(new CacheLoader<String, Node>() {
            @Override
            public Node load(String nodeId) throws Exception {
                String factoryId = SecondaryIds.nodeTypeId(db, nodeId, FactoryRegistry.MAX_TIMESTAMP);
                NodeFactory nodeFactory = getNodeFactory(factoryId);
                return nodeFactory.createNode(nodeId);
            }
        });
        nodeCache = cache.asMap();
    }

    public NodeFactory getNodeFactory(String factoryId) {
        return (NodeFactory) fetchNode(factoryId);
    }

    public Node fetchNode(String nodeId) {
        return nodeCache.get(nodeId);
    }

    public void addNode(String nodeId, Node node) {
        nodeCache.put(nodeId, node);
    }

    public void dropNode(String nodeId) {
        nodeCache.remove(nodeId);
    }
}
