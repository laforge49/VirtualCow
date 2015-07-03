package org.agilewiki.vcow.roles;

import org.agilewiki.awdb.NodeBase;
import org.agilewiki.vcow.PostRequestBlade;
import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.SimpleSimon;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for roles.
 */
public class Role_NodeInstance extends NodeBase implements Role {
    public final SimpleSimon simpleSimon;
    public String niceRoleName;
    public final Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    public final Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();

    public static String roleName(String nodeId) {
        return nodeId.substring(2, nodeId.length() - 5);
    }

    public Role_NodeInstance(String nodeId, long timestamp) {
        super(nodeId, timestamp);
        this.simpleSimon = SimpleSimon.simpleSimon;
        simpleSimon.roles.put(roleName(), this);
    }

    @Override
    public String initializeTransactionName() {
        return null;
    }

    @Override
    public SimpleSimon simpleSimon() {
        return simpleSimon;
    }

    @Override
    public String roleName() {
        return roleName(getNodeId());
    }

    @Override
    public String niceRoleName() {
        return niceRoleName;
    }

    @Override
    public Map<String, RequestBlade> requests() {
        return requests;
    }

    @Override
    public Map<String, PostRequestBlade> posts() {
        return posts;
    }

    @Override
    public RequestBlade requestBlade(String page) {
        return requests.get(page);
    }

    @Override
    public PostRequestBlade postRequestBlade(String page) {
        return posts.get(page);
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return null;
    }
}
