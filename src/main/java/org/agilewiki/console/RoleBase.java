package org.agilewiki.console;

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

    public RoleBase(SimpleSimon simpleSimon, String roleName, String niceRoleName) {
        super(simpleSimon.ooDb, "$n" + roleName + ".node");
        this.simpleSimon = simpleSimon;
        this.roleName = roleName;
        this.niceRoleName = niceRoleName;
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
