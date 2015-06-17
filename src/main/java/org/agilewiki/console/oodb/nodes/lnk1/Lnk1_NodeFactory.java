package org.agilewiki.console.oodb.nodes.lnk1;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.MetaData_Node;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class Lnk1_NodeFactory implements NodeFactory {
    public Lnk1_NodeFactory(OODb ooDb) {
        ooDb.registerNodeFactory("lnk1.node", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new MetaData_Node(nodeId, new HashMap(), factoryId);
    }
}
