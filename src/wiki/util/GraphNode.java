package wiki.util;

import java.util.LinkedList;

import wiki.gui.graph.NodeDraw;

/**
 * Graph node class
 * @author tmarques
 *
 */
public class GraphNode extends NodeDraw{
	private int id;
	private int inDeg;
	private int outDeg;
	private boolean wasVisited;
	private LinkedList<GraphEdge> adjacents;

	public GraphNode(int id) {
		super();
		setId(id);
		inDeg = 0;
		outDeg = 0;
		wasVisited = false;
		adjacents = new LinkedList<>();
	}
	/**
	 * Retrieves the value of this node
	 * @return
	 */
	public int id() {
		return id;
	}

	/**
	 * Retrieves the number of
	 * edges coming in to this node
	 * @return
	 */
	public int inDeg() {
		return inDeg;
	}

	/**
	 * Retrieves the number of edges
	 * coming of this node.
	 * @return
	 */
	public int outDeg() {
		return outDeg;
	}

	/**
	 * Set this Node has visited
	 */
	public void visit() {
		wasVisited = true;
	}

	/**
	 * Set this node as unvisited
	 */
	public void unVisit() {
		wasVisited = false;
	}

	/**
	 * Set value for this node
	 * @param value
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * If edge already exist returns false
	 * otherwise add the edge and returns true
	 * @param adjacentNode
	 * @return
	 */
	public boolean addAdjacentNode(GraphEdge adjacentNode) {
		if(edgeExists(adjacentNode))
			return false;
		adjacents.add(adjacentNode);
		adjacentNode.getToNode().inDeg++;
		outDeg++;
		return true;
	}

	/**
	 * Returns the edge linking
	 * this node to the node passed as
	 * argument of this method.
	 * @param toNode
	 * @return
	 */
	public GraphEdge getEdge(GraphNode toNode) {
		for(GraphEdge adj : adjacents)
			if(adj.getToNode() == toNode)
				return adj;

		return  null;
	}

	/**
	 * Remove the given Edge from this node to the given
	 * node, if it exists
	 * @param toNode
	 * @return
	 */
	public boolean removeAdjacentNode(GraphNode toNode) {
		if(edgeExists(new GraphEdge(this, toNode, 0))) {
			for(GraphEdge cursor : this.adjacents) {
				if(cursor.getToNode() == toNode) {
					adjacents.remove(cursor);
					outDeg--;
					cursor.getToNode().inDeg--;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if this node,
	 * already has the given edge to another node
	 * Independent of the edge value.
	 * Used in function "addAdjacentNode"
	 * @param edge
	 * @return
	 */
	private boolean edgeExists(GraphEdge edge) {
		for(GraphEdge cursor : adjacents) {
			int cursorValue = cursor.getToNode().id;
			int edgeValue = edge.getToNode().id;
			if(cursorValue == edgeValue)
				return true;
		}
		return false;
	}

	/**
	 * Returns the edges and adjacent nodes
	 * @return
	 */
	public LinkedList<GraphEdge> getAdjacentNodes() {
		return adjacents;
	}

	/**
	 * Returns <code>True</code> if this
	 * node was already visited and <code>False</code>
	 * if not
	 * @return
	 */
	public boolean wasVisited() {
		return wasVisited;
	}

	/**
	 * Returns <code>True</code> if this node has
	 * no adjacent nodes, if it is a sink.
	 * @return
	 */
	public boolean isSink() {
		if(adjacents.size() == 0)
			return true;
		return false;
	}
}