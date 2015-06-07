package org.agilewiki.console;

import org.agilewiki.console.unRole.UnRole;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Creates the metadata for the unRole.
 */
public class RecreateSystemRoleTransaction extends RecreateRoleTransaction {
    public final static String NAME = "recreateSystemRole";
    public static SystemRole systemRole;

    @Override
    public Role role() {
        return systemRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);

        Link1Id.createLink1(db, "$nuser.link1", "$n.link1", "$nsystem.role");
        SecondaryId.createSecondaryId(db, "$nuser.link1", SecondaryId.secondaryId("$n.link1", "$nuser.link1"));
        Link1Id.createLink1(db, "$n.role.link1", "$n.link1", "$nsystem.role");
        SecondaryId.createSecondaryId(db, "$n.role.link1", SecondaryId.secondaryId("$n.link1", "$n.role.link1"));
        Link1Id.createLink1(db, "$n.link1.link1", "$n.link1", "$nsystem.role");
        SecondaryId.createSecondaryId(db, "$n.link1.link1", SecondaryId.secondaryId("$n.link1", "$n.link1.link1"));
        Link1Id.createLink1(db, "$n.node.link1", "$n.link1", "$nsystem.role");
        SecondaryId.createSecondaryId(db, "$n.node.link1", SecondaryId.secondaryId("$n.link1", "$n.node.link1"));
        Link1Id.createLink1(db, "$n.nodeType.link1", "$n.link1", "$nsystem.role");
        SecondaryId.createSecondaryId(db, "$n.nodeType.link1", SecondaryId.secondaryId("$n.link1", "$n.nodeType.link1"));
        Link1Id.createLink1(db, "$n.keyType.link1", "$n.link1", "$nsystem.role");
        SecondaryId.createSecondaryId(db, "$n.keyType.link1", SecondaryId.secondaryId("$n.link1", "$n.keyType.link1"));

        SecondaryId.createSecondaryId(db, "$n.link1.link1", SecondaryId.secondaryId("$ninvDependent", "$n.link1"));
        SecondaryId.createSecondaryId(db, "$n.keyType.link1", SecondaryId.secondaryId("$ninvDependent", "$n.link1"));
        SecondaryId.createSecondaryId(db, "$n.node.link1",  SecondaryId.secondaryId("$ninvDependent", "$n.link1"));

        Link1Id.createLink1(db, "$nmetadata.node", "$n.nodeType", "$nmetadata.node");
        SecondaryId.createSecondaryId(db, "$nmetadata.node", SecondaryId.secondaryId("$n.node", "$nmetadata.node"));
        Link1Id.createLink1(db, "$n.node", "$n.nodeType", "$nmetadata.node");
        SecondaryId.createSecondaryId(db, "$n.node", SecondaryId.secondaryId("$n.node", "$n.node"));
        Link1Id.createLink1(db, "$n.role", "$n.nodeType", "$nmetadata.node");
        SecondaryId.createSecondaryId(db, "$n.role", SecondaryId.secondaryId("$n.node", "$n.role"));
        Link1Id.createLink1(db, "$n.journalEntry", "$n.nodeType", "$nmetadata.node");
        SecondaryId.createSecondaryId(db, "$n.journalEntry", SecondaryId.secondaryId("$n.node", "$n.journalEntry"));
        Link1Id.createLink1(db, "$n.link1", "$n.nodeType", "$nmetadata.node");
        SecondaryId.createSecondaryId(db, "$n.link1", SecondaryId.secondaryId("$n.node", "$n.link1"));
        Link1Id.createLink1(db, "$n.keyType", "$n.nodeType", "$nmetadata.node");
        SecondaryId.createSecondaryId(db, "$n.keyType", SecondaryId.secondaryId("$n.node", "$n.keyType"));

        Link1Id.createLink1(db, "$nuser.link1", "$n.nodeType", "$n.link1");
        SecondaryId.createSecondaryId(db, "$nuser.link1", SecondaryId.secondaryId("$n.node", "$nuser.link1"));
        Link1Id.createLink1(db, "$n.role.link1", "$n.nodeType", "$n.link1");
        SecondaryId.createSecondaryId(db, "$n.role.link1", SecondaryId.secondaryId("$n.node", "$n.role.link1"));
        Link1Id.createLink1(db, "$n.link1.link1", "$n.nodeType", "$n.link1");
        SecondaryId.createSecondaryId(db, "$n.link1.link1", SecondaryId.secondaryId("$n.node", "$n.link1.link1"));
        Link1Id.createLink1(db, "$n.node.link1", "$n.nodeType", "$n.link1");
        SecondaryId.createSecondaryId(db, "$n.node.link1", SecondaryId.secondaryId("$n.node", "$n.node.link1"));
        Link1Id.createLink1(db, "$n.nodeType.link1", "$n.nodeType", "$n.link1");
        SecondaryId.createSecondaryId(db, "$n.nodeType.link1", SecondaryId.secondaryId("$n.node", "$n.nodeType.link1"));

        Link1Id.createLink1(db, "$nsubject.keyType", "$n.keyType", "$nsystem.role");
        Link1Id.createLink1(db, "$nsubject.keyType", "$n.nodeType", "$n.keyType");
        Link1Id.createLink1(db, "$ntransactionName.keyType", "$n.keyType", "$nsystem.role");
        Link1Id.createLink1(db, "$ntransactionName.keyType", "$n.nodeType", "$n.keyType");
        Link1Id.createLink1(db, "$nemail.keyType", "$n.keyType", "$nsystem.role");
        Link1Id.createLink1(db, "$nemail.keyType", "$n.nodeType", "$n.keyType");
        Link1Id.createLink1(db, "$nrole.keyType", "$n.keyType", "$nsystem.role");
        Link1Id.createLink1(db, "$nrole.keyType", "$n.nodeType", "$n.keyType");
        Link1Id.createLink1(db, "$n.link1.keyType", "$n.keyType", "$nsystem.role");
        Link1Id.createLink1(db, "$n.link1.keyType", "$n.nodeType", "$n.keyType");
        Link1Id.createLink1(db, "$ninvDependent.keyType", "$n.keyType", "$nsystem.role");
        Link1Id.createLink1(db, "$ninvDependent.keyType", "$n.nodeType", "$n.keyType");
        Link1Id.createLink1(db, "$n.node", "$n.keyType", "$nsystem.role");
        Link1Id.createLink1(db, "$n.node", "$n.nodeType", "$n.keyType");
    }
}
