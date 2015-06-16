package org.agilewiki.console.oodb.nodes.roles;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.MetaDataNode;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class MetaDataNodeFactory implements NodeFactory {
    public MetaDataNodeFactory(OODb ooDb) {
        ooDb.registerNodeFactory("metadata.node", this);
    }

    @Override
    public Node createNode(OODb ooDb, String nodeId, String factoryId, long longTimestamp) {
        return new MetaDataNode(ooDb, nodeId, new HashMap(), factoryId, longTimestamp);
    }
}
