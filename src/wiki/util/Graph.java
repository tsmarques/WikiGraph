package wiki.util;

import java.util.LinkedList;

/*************************************
 * Class containing the implementation of
 * a Graph.
 * Might contain weighted edges
 * Might be Directed or Undirected
 * Might be a DAG
 * The id numbering of nodes goes
 * from 1 to graph size, but their
 * position on the nodes list starts in 0, so
 * the node with id = 1 is in position 0
 * @author tmarques
 ************************************/
public class Graph {
	public static final int N_MAX = 15; // maximum capacity
	private LinkedList<GraphNode> nodes;
	private LinkedList<GraphEdge> edges;
	private int size; 					// current number of nodes
	private boolean isDirected;
	private boolean isWeighted;
	
	GraphAlgorithms algorithms = GraphAlgorithms.getAlgorithms(this);

	public Graph(boolean isDirected,boolean isWeighted) {
		nodes = new LinkedList<>();
		edges = new LinkedList<>();
		size = 0;
		setGraphOrientation(isDirected);
		setGraphWeighted(isWeighted);
	}
	
	/**
	 * Set if graph edges have a weight associated
	 * @param isWeighted
	 */
	public void setGraphWeighted(boolean isWeighted) {
		this.isWeighted = isWeighted;
	}

	/**
	 * Set if this graph is directed
	 * @param isDirected
	 */
	public void setGraphOrientation(boolean isDirected){
		this.isDirected = isDirected;
	}

	/**
	 * This method is used after
	 * an algorithm is run in this graph.
	 * The nodes are marked as unvisited
	 * so it is possible to run them again.
	 */
	public void resetNodes() {
		for(GraphNode node : nodes)
			node.unVisit();
	}

	/**
	 * Returns the current size of this graph
	 * @return
	 */
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns all the nodes of this graph
	 * @return
	 */
	public LinkedList<GraphNode> getNodes() {
		return nodes;
	}

	public LinkedList<GraphEdge> getEdges() {
		return edges;
	}

	/**
	 * Returns the node specified by id
	 * @param index
	 * @return
	 */
	public GraphNode getNode(int id) {
		return nodes.get(id - 1);
	}

	/** If this graph is not full add another node.
	 * @return True if success
	 */
	public boolean addNode() {
		if(isFull())
			return false;

		size++;
		GraphNode newNode = new GraphNode(size);
		nodes.addLast(newNode);

		return true;
	}

	/**
	 * If the node exists it is removed,
	 * and is removed as adjacent from 
	 * other nodes
	 * @param node
	 * @return True if success
	 */
	public boolean removeNode(int node) {
		if(node > size) // if the node does not exist.Only relevant in in unit tests
			return false;

		// Remove the given node as adjacent from others
		for(int i = 1; i <= size; i++) {
			if(i != node) {
				GraphNode thisNode = getNode(i);
				LinkedList<GraphEdge> adjacents = (LinkedList<GraphEdge>) thisNode.getAdjacentNodes().clone();
				for(GraphEdge cursor : adjacents)
					if(cursor.getToNode().id() == node) {
						edges.remove(cursor);
						thisNode.getAdjacentNodes().remove(adjacents.indexOf(cursor));
					}
			}
		}

		for(int i = 1; i <= size; i++) { // remove edges coming and going to the given node
			if(!removeFromEdgesList(node, i))
				removeFromEdgesList(i, node);
		}

		// remove the node and correct the id of the nodes
		// from node to size.
		nodes.remove(node - 1);
		size--;
		for(int i = node; i <= size; i++)
			getNode(i).setId(i);
		return true;
	}

	/**
	 * Add edge,directed or not,between the given Nodes,
	 * if they exist.
	 * Returns <code> True </code> if successful
	 * and <code> False </code> if edge already exists
	 * @param fromNode
	 * @param toNode
	 * @return <code>True</code> or <code>False</code>
	 */
	public boolean addEdge(int fromNode, int toNode,int weight) {
		if(isDirected()) {
			GraphEdge adjacentNode = new GraphEdge(getNode(fromNode), getNode(toNode), weight);
			if(getNode(fromNode).addAdjacentNode(adjacentNode)) {
				edges.add(adjacentNode);
				return true; // if successfully added an edge
			}
			return false; // if edge already exists
		}
		else { // if the graph is not directed
			GraphEdge node1 = new GraphEdge(getNode(fromNode),getNode(toNode), weight);
			GraphEdge node2 = new GraphEdge(getNode(toNode), getNode(fromNode), weight);


			boolean n1 = getNode(fromNode).addAdjacentNode(node1);
			boolean n2 = getNode(toNode).addAdjacentNode(node2);

			if(n1 && n2) {
				edges.add(node1);
				return true; // if successfully added an edge
			}
			return false; // if edge already exists
		}
	}

	/**
	 * Remove edge between the given nodes, if
	 * it exists.
	 * Returns <code> True </code> if successful
	 * and <code> False </code> if edge does not exist.
	 * @param fromNode
	 * @param toNode
	 * @return True if success
	 */
	public boolean removeEdge(int fromNode, int toNode) {
		if(isDirected()) {
			edges.remove(getNode(fromNode).getEdge(getNode(toNode))); // remove the edge from the edge list
			return getNode(fromNode).removeAdjacentNode(getNode(toNode));
		}
		else {
			boolean n1 = getNode(fromNode).removeAdjacentNode(getNode(toNode));
			boolean n2 = getNode(toNode).removeAdjacentNode(getNode(fromNode));

			if(n1 && n2) // remove the edge from the edges list
				removeFromEdgesList(fromNode, toNode);
			return (n1 && n2);
		}
	}

	/**
	 * Change the weight of a given edge
	 * @param fromNode
	 * @param toNode
	 * @param weight
	 */
	public void changeEdgeWeight(int fromNode, int toNode, int weight) {
		if(isDirected)
			getEdge(fromNode, toNode).setWeight(weight);
		else {
			getNode(fromNode).getEdge(getNode(toNode)).setWeight(weight);
			getNode(toNode).getEdge(getNode(fromNode)).setWeight(weight);
		}
	}

	/**
	 * Removes the edge connecting
	 * the given nodes, if it exists,
	 * from the edges list.
	 * @param fromNode
	 * @param toNode
	 * @return
	 */
	private boolean removeFromEdgesList(int fromNode, int toNode) {
		int index = -1;
		for(GraphEdge cursor : edges) {
			if(cursor.getFromNode().id() == fromNode && cursor.getToNode().id() == toNode)
				index = edges.indexOf(cursor);
			if(!isDirected()) // if graph is directed doesn't need to check toNode -> fromNode
				if(cursor.getFromNode().id() == toNode && cursor.getToNode().id() == fromNode)
					index = edges.indexOf(cursor);
		}
		if(index == -1)
			return false;

		edges.remove(index);
		return true;
	}

	/**
	 * Returns the edge that connects
	 * the two given nodes, if it
	 * exists.
	 * @param fromNode
	 * @param toNode
	 * @return
	 */
	public GraphEdge getEdge(int fromNode, int toNode) {
		for(GraphEdge cursor : edges) {
			if(cursor.getFromNode().id() == fromNode)
				if(cursor.getToNode().id() == toNode)
					return cursor;

			if(!isDirected()) {
				if(cursor.getFromNode().id() == toNode)
					if(cursor.getToNode().id() == fromNode)
						return cursor;
			}

		}
		return null;
	}

	/**
	 * Returns <code>true</code> if
	 * this graph is weighted and <code>false</false> if not
	 * @return if the graph is weighted
	 */
	public boolean isWeighted() {
		return isWeighted;
	}

	/**
	 * Returns <code>true</code> if
	 * this graph is directed and <code>false</false> if not
	 * @return if graph is directed
	 */
	public boolean isDirected() {
		return isDirected;
	}

	/**
	 * Returns if this graph has
	 * an edge with negative weight
	 * @return
	 */
	public boolean hasNegativeEdges() {
		for(GraphEdge cursor : getEdges())
			if(cursor.weight() < 0)
				return true;
		return false;
	}


	/**
	 * Returns <code>True</code> if
	 * this graph cannot have more nodes.
	 * @return
	 */
	public boolean isFull() {
		return (size == N_MAX);
	}

	/**
	 * Returns the transpose of this graph
	 * @return
	 */
	public Graph transposeGraph() {
		if(isDirected()) {
			Graph transposeGraph = new Graph(isDirected(), isWeighted());
			for(GraphNode node : getNodes()) {
				transposeGraph.addNode();
				// used to redraw the nodes in the right position in GUI
				transposeGraph.getNode(node.id()).setNodeInfo(node.id(), (int)node.getCenterX(), (int)node.getCenterY());
			}

			for(GraphEdge edge : getEdges()) {
				int fromNode = edge.getToNode().id();
				int toNode = edge.getFromNode().id();
				
				transposeGraph.addEdge(fromNode, toNode, edge.weight());
			}
			return transposeGraph;
		}
		else
			return this;
	}

	/**
	 * Used for debugging.
	 * Prints all the information about
	 * this graph: Nodes, their edges,
	 * if it is weighted, directed
	 * if it has negative Edges and its size.
	 */
	public void printGraphInfo() {
		System.out.println("## Graph Info ##");
		System.out.println("Is Directed: " + isDirected());
		System.out.println("Is weighted: " + isWeighted());
		System.out.println("Size: " + size());
		for(GraphNode node : getNodes()) {
			System.out.println("Node: " + node.id());
			for(GraphEdge edge : node.getAdjacentNodes())
				System.out.println(" -> " + edge.getToNode().id() + " weight: " + edge.weight());
		}
		System.out.println("// Edges List //");
		for(GraphEdge edge : getEdges())
			System.out.println(edge.getFromNode().id() + " -> " + edge.getToNode().id() + " weight: " + edge.weight());

		System.out.println("Has negative Edges: " + hasNegativeEdges());
	}

	/**************
	 * Algorithms *
	 **************/

	public LinkedList<GraphNode> breadthFirstSearch() {
		return algorithms.breadthFirstSearch();
	}

	public LinkedList<GraphNode> depthFirstSearch() {
		return algorithms.depthFirstSearch();
	}

	/**
	 * NOTE:
	 * Prior to running this method
	 * it is needed to check if the graph
	 * is weighted and undirected. If not
	 * it shouldn't run.
	 * @param root
	 * @return
	 */
	public LinkedList<GraphEdge> primAlgorithm() {
		return algorithms.primAlgorithm();
	}

	/**
	 * NOTE:
	 * Prior to running this method
	 * it is needed to check if the graph
	 * is weighted and undirected. If not
	 * it shouldn't run
	 * @param root
	 * @return
	 */
	public int[] kruskalAlgorithm(int root) {
		return algorithms.kruskalAlgorithm();
	}

	/**
	 * Prior to running this algorithm
	 * it's needed to check if there are
	 * negative weight edges in the graph:
	 * if yes don' run.
	 * @param root
	 */
	public LinkedList<int[]> Dijkstra() {
		return algorithms.dijkstraAlgorithm();
	}

	public int[] BellmanFord(int root) {
		return algorithms.bellmanFordAlgorithm(root);
	}

	/**
	 * Not working
	 * @return
	 */
	public int[][] FloydWarshall() {
		return algorithms.floydWarshallAlgorithm();
	}

	/**
	 * Applies a BFS and returns
	 * the reachable nodes
	 * @return
	 */
	public LinkedList<GraphNode> isConnected() {
		return algorithms.breadthFirstSearch(); // use BFS to get all reachable nodes
	}

	/**
	 * Makes a Topological Sorting
	 * of this graph, if he is
	 * a DAG.
	 * @return
	 */
	public LinkedList<GraphNode> topologicalSort() {
		return algorithms.topologicalSort();
	}
	
	public LinkedList<GraphNode> limitedDepthSearch(int MAX_DEPTH) {
		return algorithms.limitedDepthSearch(MAX_DEPTH);
	}
}