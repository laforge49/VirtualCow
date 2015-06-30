package org.agilewiki.console.roles.admin.editRoles;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.PostRequestBlade;
import org.agilewiki.console.User_NodeInstance;
import org.agilewiki.console.roles.Role;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;

/**
 * Request for a journal entry.
 */
public class EditRolesBlade extends PostRequestBlade {
    public EditRolesBlade(Role role, String page) throws Exception {
        super(role, page);
        UpdateRoles_NodeFactory.create(awDb);
        awDb.registerTransaction(UpdateRoles_Node.NAME, UpdateRoles_Node.class);
    }

    @Override
    public String niceName() {
        return "Edit Roles";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            String nodeId;

            @Override
            protected String setContext() {
                nodeId = request.getParameter("nodeId");
                map.put("nodeId", nodeId);
                return "&nodeId=" + nodeId;
            }

            @Override
            protected void process()
                    throws Exception {
                User_NodeInstance user_nodeInstance = (User_NodeInstance) awDb.fetchNode(nodeId, longTimestamp);
                String email = user_nodeInstance.getEmailAddress();
                String userLink = "<a href=\"?from=" + page +
                        "&to=user" +
                        "&nodeId=" + nodeId +
                        "&role=" + roleName +
                        "#rupa\">" + email + "</a>";
                map.put("email", userLink);
                StringBuilder sb;
                while (true) {
                    try {
                        sb = new StringBuilder();
                        for (String roleName : simpleSimon.roles.keySet()) {
                            if (!roleName.equals("visitor") && !roleName.equals("system")) {
                                String niceRoleName = simpleSimon.roles.get(roleName).niceRoleName();
                                sb.append("<tr>");
                                sb.append("<td>");
                                sb.append("<input type=\"checkbox\" name=\"role-");
                                sb.append(roleName);
                                sb.append("\" value=\"");
                                sb.append(roleName);
                                sb.append("\"");
                                if (awDb.nodeHasValueId(nodeId,
                                        NameIds.ROLE_ID,
                                        NameId.generate(roleName),
                                        longTimestamp))
                                    sb.append(" checked");
                                sb.append(">");
                                sb.append("</td>");
                                sb.append("<td>");
                                sb.append(niceRoleName);
                                sb.append("</td>");
                                sb.append("</tr>\n");
                            }
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("user", sb.toString());
                finish();
            }
        }.signal();
    }

    @Override
    public void post(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            String nodeId;

            @Override
            protected String setContext() {
                nodeId = request.getParameter("nodeId");
                map.put("nodeId", nodeId);
                return "&nodeId=" + nodeId;
            }

            @Override
            protected void process()
                    throws Exception {
                MapNode mn;
                while (true) {
                    try {
                        mn = awDb.nilMap;
                        mn = mn.add(NameIds.USER_KEY, userId);
                        mn = mn.add(NameIds.NODE_ID, nodeId);
                        for (String role : simpleSimon.roles.keySet()) {
                            if (!roleName.equals("visitor") && !roleName.equals("system")) {
                                boolean o = false;
                                boolean n = false;
                                if (request.getParameter("role-" + role) != null) {
                                    n = true;
                                }
                                if (awDb.nodeHasValueId(nodeId,
                                        NameIds.ROLE_ID,
                                        NameId.generate(role),
                                        longTimestamp)) {
                                    o = true;
                                }
                                if (o && !n) {
                                    mn = mn.add(NameIds.REMOVEROLES_ID, role);
                                }
                                if (!o && n) {
                                    mn = mn.add(NameIds.ADDROLES_ID, role);
                                }
                            }
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                asyncRequestImpl.send(awDb.update(UpdateRoles_Node.NAME, mn), new AsyncResponseProcessor<String>() {
                    @Override
                    public void processAsyncResponse(String _response) throws Exception {
                        User_NodeInstance user_nodeInstance = (User_NodeInstance) awDb.fetchNode(nodeId, longTimestamp);
                        String email = user_nodeInstance.getEmailAddress();
                        String userLink = "<a href=\"?from=" + page +
                                "&to=user" +
                                "&nodeId=" + nodeId +
                                "&role=" + roleName +
                                "#rupa\">" + email + "</a>";
                        map.put("email", userLink);
                        StringBuilder sb;
                        while (true) {
                            try {
                                sb = new StringBuilder();
                                for (String roleName : simpleSimon.roles.keySet()) {
                                    if (!roleName.equals("visitor") && !roleName.equals("system")) {
                                        String niceRoleName = simpleSimon.roles.get(roleName).niceRoleName();
                                        sb.append("<tr>");
                                        sb.append("<td>");
                                        sb.append("<input type=\"checkbox\" name=\"role-");
                                        sb.append(roleName);
                                        sb.append("\" value=\"");
                                        sb.append(roleName);
                                        sb.append("\"");
                                        if (awDb.nodeHasValueId(nodeId,
                                                NameIds.ROLE_ID,
                                                NameId.generate(roleName),
                                                longTimestamp))
                                            sb.append(" checked");
                                        sb.append(">");
                                        sb.append("</td>");
                                        sb.append("<td>");
                                        sb.append(niceRoleName);
                                        sb.append("</td>");
                                        sb.append("</tr>\n");
                                    }
                                }
                                break;
                            } catch (UnexpectedChecksumException uce) {
                            }
                        }
                        map.put("user", sb.toString());
                        finish();
                    }
                });
            }
        }.signal();
    }
}
