package org.agilewiki.console.awdb;

/**
 * A factory to create a node.
 */
public interface NodeFactory extends Node {
    Node createNode(String nodeId, long timestamp);
}
