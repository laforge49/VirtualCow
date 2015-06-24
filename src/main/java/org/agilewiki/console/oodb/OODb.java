package org.agilewiki.console.oodb;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.agilewiki.console.SecondaryIds;
import org.agilewiki.jactor2.core.blades.BladeBase;
import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;
import org.agilewiki.utils.immutable.BaseRegistry;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Object Oriented Database, without state caching.
 */
public class OODb {
    public final Db db;
    private Map<String, Node> nodeCache;
    private Map<String, NodeFactory> nodeFactories = new ConcurrentHashMap<String, NodeFactory>(16, 0.75f, 1);
    private Map<String, Node> updatedNodes;
    private final DbUpdater dbUpdater;

    public OODb(int maxRootBlockSize, long maxNodeCacheSize)
            throws Exception {
        new Plant();
        dbUpdater = new DbUpdater();
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

    public void startTransaction() {
        updatedNodes = new HashMap<String, Node>();
    }

    public void updated(Node node) {
        updatedNodes.put(node.getNodeId(), node);
    }

    public void endTransaction() {
        for (Node node : updatedNodes.values()) {
            node.endTransaction();
        }
    }

    public BladeBase.AReq<String> update(String transactionName, MapNode tMapNode) {
        return dbUpdater.update(transactionName, tMapNode);
    }

    private class DbUpdater extends NonBlockingBladeBase {
        public DbUpdater() throws Exception {}

        public AReq<String> update(String transactionName, MapNode tMapNode) {
            tMapNode = tMapNode.add(Db.transactionNameId, transactionName);
            return update(tMapNode.toByteBuffer());
        }

        public AReq<String> update(ByteBuffer tByteBuffer) {
            return new AReq<String>("update") {
                @Override
                protected void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                                     AsyncResponseProcessor<String> _asyncResponseProcessor) {
                    startTransaction();
                    _asyncRequestImpl.send(db.update(tByteBuffer), new AsyncResponseProcessor<String>() {
                        @Override
                        public void processAsyncResponse(String _response) throws Exception {
                            endTransaction();
                            _asyncResponseProcessor.processAsyncResponse(_response);
                        }
                    });
                }
            };
        }
    }
}
