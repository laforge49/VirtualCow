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
abstract public class RoleBase extends NodeBase implements Role {
    public final SimpleSimon simpleSimon;
    public final String roleName;
    public final String niceRoleName;
    public final Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    public final Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();

    public RoleBase(SimpleSimon simpleSimon, String roleId, Map<String, String> parameters) {
        super(roleId, parameters);
        this.simpleSimon = simpleSimon;
        this.roleName = roleId.substring(2, roleId.length() - 5);
        this.niceRoleName = parameters.get("niceRoleName");
        simpleSimon.roles.put(roleName, this);
    }

    @Override
    public SimpleSimon simpleSimon() {
        return simpleSimon;
    }

    @Override
    public String roleName() {
        return roleName;
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
}
