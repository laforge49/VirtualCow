package org.agilewiki.console;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for roles.
 */
abstract public class RoleBase implements Role {
    public final SimpleSimon simpleSimon;
    public final String roleName;
    public final Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    public final Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();

    public RoleBase(SimpleSimon simpleSimon, String roleName) {
        this.simpleSimon = simpleSimon;
        this.roleName = roleName;
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
