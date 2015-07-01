package org.agilewiki.vcow.roles.admin;

import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.PostRequestBlade;
import org.agilewiki.vcow.SimpleSimon;
import org.agilewiki.vcow.roles.Role;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.awdb.db.immutable.collections.MapNode;

import javax.servlet.AsyncContext;
import java.util.Map;

/**
 * Request for a non-performing journal entry.
 */
public class RecreateRoleBlade extends PostRequestBlade {
    public RecreateRoleBlade(Role role, String page) throws Exception {
        super(role, page);
    }

    @Override
    public String niceName() {
        return "Recreate Role";
    }

    String option(String roleName, String niceName, boolean selected) {
        StringBuilder sb = new StringBuilder();
        sb.append("<option value=\"");
        sb.append(roleName);
        sb.append("\"");
        if (selected)
            sb.append(" selected");
        sb.append(">");
        sb.append(niceName);
        sb.append("</option>");
        return sb.toString();
    }

    String options(Role role, String selectedRole) {
        SimpleSimon simpleSimon = role.simpleSimon();
        Map<String, Role> roles = simpleSimon.roles;
        StringBuilder sb = new StringBuilder();
        sb.append(option("", "-select role-", false));
        for (String roleName : roles.keySet()) {
            Role r = roles.get(roleName);
            sb.append(option(roleName, r.niceRoleName(), roleName.equals(selectedRole)));
        }
        return sb.toString();
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                map.put("options", options(role, ""));
                finish();
            }
        }.signal();
    }

    @Override
    public void post(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                String selectedRole = request.getParameter("selectedRole");
                map.put("options", options(role, ""));
                Role sRole = simpleSimon.roles.get(selectedRole);
                if (selectedRole.length() == 0 ||
                        sRole == null) {
                    map.put("error", "Select a role");
                    finish();
                    return;
                }

                MapNode mn = awDb.nilMap;
                if (userId != null)
                    mn = mn.add(NameIds.USER_KEY, userId);
                mn = mn.add(NameIds.REMOTE_HOST, request.getRemoteHost());
                mn = mn.add(NameIds.REMOTE_ADDR, request.getRemoteAddr());
                mn = mn.add(NameIds.REMOTE_PORT, request.getRemotePort());
                asyncRequestImpl.send(awDb.update(sRole.initializeTransactionName(), mn),
                        new AsyncResponseProcessor<String>() {
                            @Override
                            public void processAsyncResponse(String _response) throws Exception {
                                map.put("success", sRole.niceRoleName() + " recreated");
                                finish();
                            }
                        });

            }
        }.signal();
    }
}
