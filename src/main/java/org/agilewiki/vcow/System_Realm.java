package org.agilewiki.vcow;

import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.nodes.Lnk1_NodeFactory;
import org.agilewiki.awdb.nodes.Realm_Node;
import org.agilewiki.awdb.nodes.User_NodeFactory;

public class System_Realm extends Realm_Node {
    public System_Realm(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void newNode(Node node, String userId) {
        if (!User_NodeFactory.SYSTEM_USER_ID.equals(userId)) {
            System.out.println(node.getNodeId() + " ``````````````````````````````````````````");
            node.createLnk1(Lnk1_NodeFactory.OF_DOMAIN_ID, NameIds.USERS_SYSTEM_DOMAIN_ID);
        }
    }
}
