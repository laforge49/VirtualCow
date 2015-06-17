package org.agilewiki.console.oodb.nodes.lnk1;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class OfRole_Lnk1Factory implements NodeFactory {
    public OfRole_Lnk1Factory(OODb ooDb) {
        ooDb.registerNodeFactory("ofRole.lnk1", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new OfRole_Lnk1(nodeId, new HashMap(), factoryId);
    }
}
