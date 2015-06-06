package org.agilewiki.console;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for roles.
 */
abstract public class RoleBase implements Role {
    public final SimpleSimon simpleSimon;
    public final Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    public final Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();

    public RoleBase(SimpleSimon simpleSimon) {
        this.simpleSimon = simpleSimon;
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
