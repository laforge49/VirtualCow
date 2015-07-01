package org.agilewiki.vcow;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.Delete;
import org.agilewiki.awdb.nodes.Attribute_NodeFactory;
import org.agilewiki.awdb.nodes.Key_NodeFactory;
import org.agilewiki.awdb.nodes.Lnk1_NodeFactory;
import org.agilewiki.vcow.roles.Role;
import org.agilewiki.vcow.roles.Role_NodeFactory;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;

/**
 * Creates the common metadata for a role.
 */
abstract public class RecreateRole_Node extends VCJournalEntry_Node {

    public RecreateRole_Node() {
    }

    public RecreateRole_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    abstract public Role role();

    public void defineNode(String nodeId, String nodeType, String superType, String roleId, String... attributes) {
        AwDb awDb = getAwDb();
        if (nodeType != null) {
            awDb.createSecondaryId(nodeId, Key_NodeFactory.NODETYPE_ID, nodeType);
        }
        if (superType != null) {
            awDb.createSecondaryId(nodeId, Key_NodeFactory.SUPERTYPE_ID, superType);
        }
        awDb.createLnk1(nodeId,
                NameIds.OFROLE_ID,
                roleId);
        for (String attributeName : attributes) {
            Attribute_NodeFactory.define(attributeName, nodeId);
        }
    }

    public void defineKey(String nodeId, String targetType, String roleId) {
        defineNode(nodeId, Key_NodeFactory.ID, null, roleId);
        getAwDb().createLnk1(nodeId,
                Lnk1_NodeFactory.TARGET_ID,
                targetType);
    }

    public void defineLnk1(String nodeId, String invDependency, String originType, String destinationType, String ofRole) {
        AwDb awDb = getAwDb();
        defineNode(nodeId, Lnk1_NodeFactory.ID, null, ofRole);
        if (invDependency != null) {
            awDb.createSecondaryId(nodeId, Key_NodeFactory.INVDEPENDENCY_ID, invDependency);
        }
        awDb.createLnk1(nodeId,
                Lnk1_NodeFactory.ORIGIN_ID,
                originType);
        awDb.createLnk1(nodeId,
                Lnk1_NodeFactory.DESTINATION_ID,
                destinationType);
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        final String thisRoleId = "$n" + role().roleName();
        final String thisRoleNodeId = thisRoleId + ".role";

        Delete.delete(thisRoleNodeId);

        getAwDb().createSecondaryId(thisRoleNodeId, Key_NodeFactory.NODETYPE_ID, Role_NodeFactory.ID);
    }
}
