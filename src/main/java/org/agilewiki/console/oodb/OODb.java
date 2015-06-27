package org.agilewiki.console.oodb;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.agilewiki.console.oodb.nodes.Key_Node;
import org.agilewiki.jactor2.core.blades.BladeBase;
import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.ExceptionHandler;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.BaseRegistry;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.ListNode;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.immutable.collections.VersionedListNode;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.DbFactoryRegistry;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Object Oriented Database, without state caching.
 */
public class OODb {
    public final Db db;
    private LoadingCache<String, Node> nodeCache;
    private Map<String, Node> updatedNodes;
    private final DbUpdater dbUpdater;
    private final DbFactoryRegistry dbFactoryRegistry;
    public final VersionedListNode versionedNilList;
    public final VersionedMapNode versionedNilMap;
    public final ListNode nilList;
    public final MapNode nilMap;

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

        dbFactoryRegistry = db.dbFactoryRegistry;
        versionedNilList = dbFactoryRegistry.versionedNilList;
        versionedNilMap = dbFactoryRegistry.versionedNilMap;
        nilList = dbFactoryRegistry.nilList;
        nilMap = dbFactoryRegistry.nilMap;

        final LoadingCache<String, Node> cache = CacheBuilder.newBuilder().concurrencyLevel(1).
                softValues().build(new CacheLoader<String, Node>() {
            @Override
            public Node load(String nodeId) throws Exception {
                String factoryId = nodeTypeId(nodeId);
                if (factoryId == null) {
                    return NullNode.singleton;
                }
                NodeFactory nodeFactory = getNodeFactory(factoryId);
                if (nodeFactory == null)
                    return NullNode.singleton;
                Node node = nodeFactory.createNode(nodeId);
                if (node == null)
                    return NullNode.singleton;
                return node;
            }
        });
        nodeCache = cache;
    }

    public void close() {
        db.close();
    }

    public void registerTransaction(String transactionName, Class transactionClass) {
        db.registerTransaction(transactionName, transactionClass);
    }

    public NodeData newNodeData(String nodeId) {
        return new NodeData(db, nodeId);
    }

    public boolean isPrivileged() {
        return db.isPrivileged();
    }

    public void checkPrivilege() {
        db.checkPrivilege();
    }

    String nodeTypeId(String nodeId) {
        for (String mnId : db.keysIterable(SecondaryId.secondaryInv(nodeId, Key_Node.NODETYPE_ID),
                FactoryRegistry.MAX_TIMESTAMP)) {
            return mnId;
        }
        return null;
    }

    public NodeFactory getNodeFactory(String factoryId) throws ExecutionException {
        return (NodeFactory) fetchNode(factoryId);
    }

    public Node fetchNode(String nodeId) {
        char x = nodeId.charAt(1);
        if (x != 'n' && x != 'r' && x != 't')
            return null;
        Node node = nodeCache.getUnchecked(nodeId);
        if (node instanceof NullNode) {
            dropNode(nodeId);
            return null;
        }
        return node;
    }

    public void addNode(String nodeId, Node node) {
        nodeCache.put(nodeId, node);
    }

    public void dropNode(String nodeId) {
        nodeCache.invalidate(nodeId);
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

    public void reset() {
        for (Node node : updatedNodes.values()) {
            node.reset();
        }
    }

    public void clearMap(String nodeId) {
        Node node = fetchNode(nodeId);
        if (node == null)
            db.clearMap(nodeId);
        else
            node.clearMap();
    }

    public void set(String nodeId, String key, Object value) {
        Node node = fetchNode(nodeId);
        if (node == null)
            db.set(nodeId, key, value);
        else
            node.set(key, value);
    }

    public Object get(String nodeId, String key, long timestamp) {
        if (timestamp != FactoryRegistry.MAX_TIMESTAMP)
            return db.get(nodeId, key, timestamp);
        Node node = fetchNode(nodeId);
        if (node == null)
            return db.get(nodeId, key, timestamp);
        return node.get(key);
    }

    public List getFlatList(String nodeId, String key, long timestamp) {
        if (timestamp != FactoryRegistry.MAX_TIMESTAMP) {
            VersionedListNode vln = db.versionedListNode(nodeId, key);
            if (vln == null)
                return new ArrayList();
            return vln.flatList(timestamp);
        }
        Node node = fetchNode(nodeId);
        if (node == null) {
            VersionedListNode vln = db.versionedListNode(nodeId, key);
            if (vln == null)
                return new ArrayList();
            return vln.flatList(timestamp);
        }
        return node.getFlatList(key);
    }

    public NavigableMap<Comparable, List> getFlatMap(String nodeId, long timestamp) {
        if (timestamp != FactoryRegistry.MAX_TIMESTAMP) {
            VersionedMapNode vmn = db.get(nodeId);
            if (vmn == null)
                return new TreeMap<>();
            else
                return vmn.flatMap(FactoryRegistry.MAX_TIMESTAMP);
        }
        Node node = fetchNode(nodeId);
        if (node == null) {
            VersionedMapNode vmn = db.get(nodeId);
            if (vmn == null)
                return new TreeMap<>();
            else
                return vmn.flatMap(FactoryRegistry.MAX_TIMESTAMP);
        }
        return node.getFlatMap();
    }

    public void createSecondaryId(String nodeId, String keyId, String valueId) {
        Node node = fetchNode(nodeId);
        if (node == null)
            SecondaryId.createSecondaryId(db, nodeId, SecondaryId.secondaryId(keyId, valueId));
        else
            node.createSecondaryId(keyId, valueId);
    }

    public void removeSecondaryId(String nodeId, String keyId, String valueId) {
        Node node = fetchNode(nodeId);
        if (node == null)
            SecondaryId.removeSecondaryId(db, nodeId, SecondaryId.secondaryId(keyId, valueId));
        else
            node.removeSecondaryId(keyId, valueId);
    }

    public Iterable<String> keyIdIterable(String nodeId) {
        Node node = fetchNode(nodeId);
        if (node == null) {
            return SecondaryId.typeIdIterable(db, nodeId);
        }
        return node.keyIdIterable();
    }

    public String getKeyValue(String nodeId, String keyId, long timestamp) {
        if (timestamp != FactoryRegistry.MAX_TIMESTAMP) {
            for (String value : db.keysIterable(SecondaryId.secondaryInv(nodeId, keyId), timestamp)) {
                return value;
            }
            return null;
        }
        Node node = fetchNode(nodeId);
        if (node == null) {
            if (Key_Node.NODETYPE_ID.equals(keyId)) {
                return null;
            }
            for (String value : db.keysIterable(SecondaryId.secondaryInv(nodeId, keyId), timestamp)) {
                return value;
            }
            return null;
        }
        return node.getKeyValue(keyId);
    }

    public String getKeyTarget(String keyId, String valueId, long timestamp) {
        for (String targetId : db.keysIterable(SecondaryId.secondaryId(keyId, valueId), timestamp)) {
            return targetId;
        }
        return null;
    }

    public boolean isNode(String id, long longTimestamp) {
        return getKeyValue(id, Key_Node.NODETYPE_ID, longTimestamp) != null;
    }

    public String kindId(String nodeId, long longTimestamp) {
        String kind = getKeyValue(nodeId, Key_Node.SUPERTYPE_ID, longTimestamp);
        if (kind == null)
            kind = getKeyValue(nodeId, Key_Node.NODETYPE_ID, longTimestamp);
        return kind;
    }

    public boolean hasKey(String nodeId, String keyId, long timestamp) {
        if (timestamp != FactoryRegistry.MAX_TIMESTAMP) {
            return db.keysIterable(SecondaryId.secondaryInv(nodeId, keyId), db.getTimestamp()).hasNext();
        }
        Node node = fetchNode(nodeId);
        if (node == null) {
            return db.keysIterable(SecondaryId.secondaryInv(nodeId, keyId), db.getTimestamp()).hasNext();
        }
        return node.hasKey(keyId);
    }

    public boolean hasKeyValue(String nodeId, String keyId, String valueId, long timestamp) {
        if (timestamp != FactoryRegistry.MAX_TIMESTAMP) {
            return SecondaryId.hasSecondaryId(db, nodeId, keyId, valueId, timestamp);
        }
        Node node = fetchNode(nodeId);
        if (node == null) {
            return SecondaryId.hasSecondaryId(db, nodeId, keyId, valueId, timestamp);
        }
        return node.hasKeyValue(keyId, valueId);
    }

    public boolean hasKeyTarget(String keyId, String valueId, long timestamp) {
        for (String targetId : db.keysIterable(SecondaryId.secondaryId(keyId, valueId), timestamp)) {
            return true;
        }
        return false;
    }

    public Iterable<String> keyValueIdIterable(String nodeId, String keyId, long timestamp) {
        if (timestamp != FactoryRegistry.MAX_TIMESTAMP) {
            return db.keysIterable(SecondaryId.secondaryInv(nodeId, keyId), timestamp);
        }
        Node node = fetchNode(nodeId);
        if (node == null) {
            return db.keysIterable(SecondaryId.secondaryInv(nodeId, keyId), timestamp);
        }
        return node.keyValueIdIterable(keyId);
    }

    public Iterable<String> keyTargetIdIterable(String keyId, String valueId, long timestamp) {
        return db.keysIterable(SecondaryId.secondaryId(keyId, valueId), timestamp);
    }

    public void createLnk1(String originNodeId, String labelId, String destinationNodeId) {
        Node node = fetchNode(originNodeId);
        if (node == null)
            Link1Id.createLink1(db, originNodeId, labelId, destinationNodeId);
        else
            node.createLnk1(labelId, destinationNodeId);
    }

    public void removeLnk1(String originNodeId, String labelId, String destinationNodeId) {
        Node node = fetchNode(originNodeId);
        if (node == null)
            Link1Id.removeLink1(db, originNodeId, labelId, destinationNodeId);
        else
            node.removeLnk1(labelId, destinationNodeId);
    }

    public Iterable<String> label1IdIterable(String nodeId) {
        Node node = fetchNode(nodeId);
        if (node == null) {
            return Link1Id.link1LabelIdIterable(db, nodeId);
        }
        return node.label1IdIterable();
    }

    public boolean hasLabel1(String nodeId, String label1Id, long timestamp) {
        if (timestamp != FactoryRegistry.MAX_TIMESTAMP) {
            return db.keysIterable(Link1Id.link1Id(nodeId, label1Id), timestamp).hasNext();
        }
        Node node = fetchNode(nodeId);
        if (node == null) {
            return db.keysIterable(Link1Id.link1Id(nodeId, label1Id), timestamp).hasNext();
        }
        return node.hasLabel1(label1Id);
    }

    public boolean hasDestination(String nodeId, String label1Id, String destinationId, long timestamp) {
        if (timestamp != FactoryRegistry.MAX_TIMESTAMP) {
            return Link1Id.hasLink1(db, nodeId, label1Id, destinationId, timestamp);
        }
        Node node = fetchNode(nodeId);
        if (node == null) {
            return Link1Id.hasLink1(db, nodeId, label1Id, destinationId, timestamp);
        }
        return node.hasDestination(label1Id, destinationId);
    }

    public Iterable<String> destinationIdIterable(String nodeId, String label1Id, long timestamp) {
        if (timestamp != FactoryRegistry.MAX_TIMESTAMP) {
            return Link1Id.link1IdIterable(db, nodeId, label1Id, timestamp);
        }
        Node node = fetchNode(nodeId);
        if (node == null) {
            return Link1Id.link1IdIterable(db, nodeId, label1Id, timestamp);
        }
        return node.destinationIdIterable(label1Id);
    }

    public BladeBase.AReq<String> update(String transactionName, MapNode tMapNode) {
        return dbUpdater.update(transactionName, tMapNode);
    }

    private class DbUpdater extends NonBlockingBladeBase {
        public DbUpdater() throws Exception {
        }

        public AReq<String> update(String transactionName, MapNode tMapNode) {
            tMapNode = tMapNode.add(Db.transactionNameId, transactionName);
            return update(tMapNode.toByteBuffer());
        }

        public AReq<String> update(ByteBuffer tByteBuffer) {
            return new AReq<String>("update") {
                @Override
                protected void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                                     AsyncResponseProcessor<String> _asyncResponseProcessor) {
                    _asyncRequestImpl.setExceptionHandler(new ExceptionHandler() {
                        @Override
                        public Object processException(Exception e) throws Exception {
                            reset();
                            return super.processException(e);
                        }
                    });
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
