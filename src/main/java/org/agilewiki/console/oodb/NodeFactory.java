package org.agilewiki.console.oodb;

/**
 * A factory to create a node.
 */
public interface NodeFactory {
    Node createNode(String nodeId, String factoryId);
}
