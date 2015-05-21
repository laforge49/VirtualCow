package org.agilewiki.console.maintenance;

import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.profile.ProfileRole;
import org.agilewiki.console.requests.PostRequestBlade;
import org.agilewiki.console.requests.RequestBlade;

import java.util.HashMap;
import java.util.Map;

/**
 * The maintenance role.
 */
public class MaintenanceRole implements Role {

    private final ProfileRole profileRole;
    private Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    private Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();
    private HomeBlade homeBlade;

    public MaintenanceRole(SimpleSimon simpleSimon)
            throws Exception {
        profileRole = simpleSimon.profileRole;

        HomeBlade homeBlade;
        JournalBlade journalBlade;
        NodeBlade nodeBlade;
        PostBlade postBlade;
        SecondaryKeysBlade secondaryKeysBlade;
        NodesBlade nodesBlade;
        InvLinksBlade invLinksBlade;

        homeBlade = new HomeBlade(simpleSimon);
        postBlade = new PostBlade(simpleSimon);
        nodeBlade = new NodeBlade(simpleSimon);
        journalBlade = new JournalBlade(simpleSimon);
        secondaryKeysBlade = new SecondaryKeysBlade(simpleSimon);
        nodesBlade = new NodesBlade(simpleSimon);
        invLinksBlade = new InvLinksBlade(simpleSimon);

        requests.put("home", homeBlade);
        requests.put("post", postBlade);
        requests.put("node", nodeBlade);
        requests.put("journal", journalBlade);
        requests.put("secondaryKeys", secondaryKeysBlade);
        requests.put("nodes", nodesBlade);
        requests.put("invLinks", invLinksBlade);

        posts.put("post", postBlade);
    }

    @Override
    public RequestBlade requestBlade(String page) {
        RequestBlade rb = requests.get(page);
        if (rb == null)
            rb = profileRole.requestBlade(page);
        return rb;
    }

    @Override
    public PostRequestBlade postRequestBlade(String page) {
        PostRequestBlade rb = posts.get(page);
        if (rb == null)
            rb = profileRole.postRequestBlade(page);
        return rb;
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return homeBlade;
    }

    @Override
    public String getDefaultRequestPage() {
        return "home";
    }
}
