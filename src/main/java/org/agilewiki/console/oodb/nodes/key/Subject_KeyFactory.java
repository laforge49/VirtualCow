package org.agilewiki.console.oodb.nodes.key;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class Subject_KeyFactory implements NodeFactory {
    public Subject_KeyFactory(OODb ooDb) {
        ooDb.registerNodeFactory("subject.key", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Subject_Key(nodeId, new HashMap(), factoryId);
    }
}
