package org.agilewiki.console.oodb.nodes.roles;

import org.agilewiki.console.PostRequestBlade;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.NodeBase;

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

    public Role_NodeInstance(String nodeId, String factoryId) {
        super(nodeId, factoryId);
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
        return nodeId.substring(2, nodeId.length() - 9);
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
