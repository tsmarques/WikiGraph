package wiki.util;

import wiki.gui.graph.EdgeDraw;

/*********************
 * Edge class
 * @author tmarques
 * [COMPLETE]
 *********************/
public class GraphEdge extends EdgeDraw {
	private int weight;
	private GraphNode fromNode;
	private GraphNode toNode;
	
	public GraphEdge(GraphNode fromNode, GraphNode toNode, int weight) {
		setNodes(fromNode,toNode);
		setWeight(weight);
	}
	
	/**
	 * Sets the nodes of
	 * this edge
	 * @param toNode
	 */
	private void setNodes(GraphNode fromNode, GraphNode toNode) {
		this.fromNode = fromNode;
		this.toNode = toNode;
	}
	
	/**
	 * Set the weight/cost of this edge.
	 * If the graph is now weighted this
	 * will be set to 1.
	 * @param weight
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public GraphNode getFromNode() {
		return fromNode;
	}
	
	public GraphNode getToNode() {
		return toNode;
	}
	
	public int weight() {
		return weight;
	}
 }