package org.agilewiki.console.oodb;

/**
 * A factory to create a node.
 */
public interface NodeFactory extends Node {
    Node createNode(String nodeId, long timestamp);
}
