package org.agilewiki.console.admin;

import org.agilewiki.console.*;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.ListNode;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Request for a journal entry.
 */
public class EditRolesBlade extends PostRequestBlade {
    public EditRolesBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    protected String groupName() {
        return "admin";
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
                String email = User.email(db, nodeId, longTimestamp);
                String userLink = "<a href=\"?from=" + page +
                        "&to=user" +
                        "&nodeId=" + nodeId +
                        "&role=" + roleName +
                        "\">" + email + "</a>";
                map.put("email", userLink);
                StringBuilder sb;
                while (true) {
                    try {
                        sb = new StringBuilder();
                        for (String roleName: simpleSimon.roles.keySet()) {
                            if (roleName.equals("unRole"))
                                break;
                            String niceRoleName = simpleSimon.roles.get(roleName).niceRoleName();
                            sb.append("<tr>");
                            sb.append("<td>");
                            sb.append("<input type=\"checkbox\" name=\"roles\" value=\"");
                            sb.append(this.roleName);
                            sb.append("\"");
                            if (SecondaryId.hasSecondaryId(
                                    db,
                                    nodeId,
                                    SecondaryId.secondaryId(User.ROLE_ID, NameId.generate(roleName)),
                                    longTimestamp))
                                sb.append(" checked");
                            sb.append(">");
                            sb.append("</td>");
                            sb.append("<td>");
                            sb.append(niceRoleName);
                            sb.append("</td>");
                            sb.append("</tr>\n");
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
                ListNode addRoles = db.dbFactoryRegistry.nilList;
                ListNode removeRoles = db.dbFactoryRegistry.nilList;
                for (String role: simpleSimon.roles.keySet()) {
                    boolean o = false;
                    boolean n = false;
                    if (request.getParameter("role-"+role) != null) {
                        n = true;
                    }
                    if (SecondaryId.hasSecondaryId(
                            db,
                            nodeId,
                            SecondaryId.secondaryId(User.ROLE_ID, NameId.generate(role)),
                            longTimestamp))
                    {
                        o = true;
                    }
                    if (o && !n) {
                        removeRoles = removeRoles.add(role);
                    }
                    if (!o && n) {
                        addRoles = addRoles.add(role);
                    }
                }
                MapNode mn = db.dbFactoryRegistry.nilMap;
                mn = mn.add(User.USER_KEY, userId);
                mn = mn.add("nodeId", nodeId);
                mn = mn.add("addRoles", addRoles);
                mn = mn.add("removeRoles", removeRoles);



                String email = User.email(db, nodeId, longTimestamp);
                String userLink = "<a href=\"?from=" + page +
                        "&to=user" +
                        "&nodeId=" + nodeId +
                        "&role=" + roleName +
                        "\">" + email + "</a>";
                map.put("email", userLink);
                StringBuilder sb;
                while (true) {
                    try {
                        sb = new StringBuilder();
                        for (String roleName: simpleSimon.roles.keySet()) {
                            if (roleName.equals("unRole"))
                                break;
                            String niceRoleName = simpleSimon.roles.get(roleName).niceRoleName();
                            sb.append("<tr>");
                            sb.append("<td>");
                            sb.append("<input type=\"checkbox\" name=\"role-");
                            sb.append(roleName);
                            sb.append("\" value=\"");
                            sb.append(roleName);
                            sb.append("\"");
                            if (SecondaryId.hasSecondaryId(
                                    db,
                                    nodeId,
                                    SecondaryId.secondaryId(User.ROLE_ID, NameId.generate(roleName)),
                                    longTimestamp))
                                sb.append(" checked");
                            sb.append(">");
                            sb.append("</td>");
                            sb.append("<td>");
                            sb.append(niceRoleName);
                            sb.append("</td>");
                            sb.append("</tr>\n");
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
}
