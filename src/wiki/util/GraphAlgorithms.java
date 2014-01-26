package wiki.util;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class implements singleton pattern.
 * All the algorithms applied in a graph
 * are written here.
 * @author tm
 *
 */
public class GraphAlgorithms {
	private Graph graph;
	private static GraphAlgorithms thisInstance;

	private GraphAlgorithms(Graph graph) {
		this.graph = graph;
	}

	public static GraphAlgorithms getAlgorithms(Graph graph) {
		if(thisInstance != null)
			return thisInstance;
		else
			return new GraphAlgorithms(graph);
	}

	public LinkedList<GraphNode> breadthFirstSearch() {
		Queue<GraphNode> queue = new LinkedList<>();
		LinkedList<GraphNode> solution = new LinkedList<>(); // this list is used to print the solution in the GUI
		queue.add(graph.getNode(1));
		solution.add(graph.getNode(1));
		graph.getNode(1).visit();

		while(!queue.isEmpty()) {
			GraphNode node = queue.poll();
			for(GraphEdge cursor : node.getAdjacentNodes()) {
				if(!cursor.getToNode().wasVisited()) {
					cursor.getToNode().visit();
					queue.add(cursor.getToNode());
					solution.addLast(cursor.getToNode());
				}
			}
		}
		graph.resetNodes();	
		return solution;
	}

	public LinkedList<GraphNode> depthFirstSearch() {
		LinkedList<GraphNode> list = new LinkedList<>();
		LinkedList<GraphNode> solution = new LinkedList<>();
		list.addFirst(graph.getNode(1));
		solution.add(graph.getNode(1));
		graph.getNode(1).visit();

		while(!list.isEmpty()) {
			GraphNode node = list.removeFirst();
			if(!solution.contains(node))
				solution.addLast(node);
			for(GraphEdge cursor : node.getAdjacentNodes()) {
				if(!cursor.getToNode().wasVisited()) {
					cursor.getToNode().visit();
					list.addFirst(cursor.getToNode());
				}
			}
		}
		graph.resetNodes();
		return solution;
	}

	public LinkedList<GraphEdge> primAlgorithm() {
		int[] tmpSolution = new int[graph.size() + 1];
		LinkedList<GraphEdge> solution = new LinkedList<>();
		int visitedNodes = 0;
		Heap heap = new Heap(graph.size());

		// initialize heap nodes and solution array
		for(int i = 1; i <= graph.size(); i++) {
			heap.add(i, Integer.MAX_VALUE);
			tmpSolution[i] = 0;
		}
		heap.decreasePriority(1, 0); // set root node priority from +inf to 0

		while(visitedNodes != graph.size()) {
			GraphNode node = graph.getNode(heap.extractMin());
			node.visit();
			visitedNodes++;
			if(node.outDeg() != 0) { // if this node has adjacents
				for(GraphEdge cursor : node.getAdjacentNodes()) { // for the adjacents to the node
					GraphNode adjacentNode = cursor.getToNode();
					if(!adjacentNode.wasVisited()) {
						HeapNode tempNode = heap.getNode(adjacentNode.id()); // heap node with the adjacent
						if(cursor.weight() < tempNode.priority()) {
							heap.decreasePriority(adjacentNode.id(), cursor.weight());
							tmpSolution[adjacentNode.id()] = node.id();
						}
					}
				}
			}
		}
		graph.resetNodes();
		for(int i = 2; i < tmpSolution.length; i++)
			solution.addLast(graph.getEdge(i, tmpSolution[i]));
		return solution;
	}

	public int[] kruskalAlgorithm() {
		int[] solution = new int[graph.size() + 1];
		int nNodes = 0; // number of nodes connected
		Heap heap = new Heap(graph.size());
		DisjointSet set = new DisjointSet(graph.size());

		for(int i = 1; i <= graph.size(); i++) {
			GraphEdge edge = graph.getEdges().get(i - 1);
			heap.addEdge(edge, i, edge.weight());
			solution[i] = 0;
		}

		while(nNodes <= graph.size()) {
			GraphEdge edge = heap.extractMinEdge();
			int fromNode = edge.getFromNode().id();
			int toNode = edge.getFromNode().id();

			if(set.find(fromNode) != set.find(toNode)) {
				set.union(fromNode, toNode);
				nNodes++;
				solution[toNode] = fromNode;
			}
		}
		graph.resetNodes();
		return solution;
	}

	public LinkedList<int[]> dijkstraAlgorithm() {
		LinkedList<int[]> solution = new LinkedList<>(); // this list holds the array with the node predecessors and the array with edges cost
		int[] predecessor = new int[graph.size() + 1]; // precessor[i] holds the predecessor node of i
		int[] cost = new int[graph.size() + 1]; // cost[i] holds the min cost to go to i
		Heap heap = new Heap(graph.size());
		for(int i = 1; i <= graph.size(); i++) {
			if(i != 1)
				cost[i] = Integer.MAX_VALUE;
			else
				cost[i] = 0;
			predecessor[i] = 0;
			heap.add(i, cost[i]);
		}

		while(!heap.isEmpty()) {
			GraphNode min = graph.getNode(heap.extractMin());
			min.visit();
			for(GraphEdge cursor : min.getAdjacentNodes()) {
				GraphNode adjacent = cursor.getToNode();
				if(!adjacent.wasVisited()) {
					int totalCost = cost[min.id()] + cursor.weight();
					if(totalCost < cost[adjacent.id()]) {
						predecessor[adjacent.id()] = min.id();
						cost[adjacent.id()] = totalCost;

						heap.decreasePriority(adjacent.id(), cost[adjacent.id()]);
					}
				}
			}
		}
		solution.addLast(predecessor);
		solution.addLast(cost);
		graph.resetNodes();
		return solution;
	}

	public int[] bellmanFordAlgorithm(int root) {
		int[] solution = new int[graph.size() +1];
		int[] cost = new int[graph.size() + 1];

		for(int i = 1; i <= graph.size(); i++) {
			solution[i] = 0;
			cost[i] = Integer.MAX_VALUE;
		}
		cost[root] = 0;

		for(int i = 1; i < graph.size(); i++) {
			for(GraphNode node : graph.getNodes()) {
				if(cost[node.id()] != Integer.MAX_VALUE) {
					for(GraphEdge edge : node.getAdjacentNodes()) {
						int totalCost = cost[node.id()] + edge.weight();
						int adjacent = edge.getToNode().id();
						if(totalCost < cost[adjacent]) {
							cost[adjacent] = totalCost;
							solution[adjacent] = node.id();
						}
					}
				}
			}
		}
		// validate result in order
		// to find negative cycles
		for(GraphNode node : graph.getNodes()) {
			if(cost[node.id()] != Integer.MAX_VALUE) {
				for(GraphEdge edge : node.getAdjacentNodes()) {
					int totalCost = cost[node.id()] + edge.weight();
					int adjacent = edge.getToNode().id();
					// if it's still possible to improve the weight found, it's because there's a negative cycle
					if(cost[adjacent] > totalCost)
						return null;
				}
			}
		}
		return solution;
	}

	/**
	 * Node used for now
	 * @return
	 */
	public int[][] floydWarshallAlgorithm() {
		//int[][] matAdj = new int[size + 1][size + 1];
		int[][] solution = new int[graph.size() + 1][graph.size() + 1];
		int[][] cost = new int[graph.size() + 1][graph.size() + 1];
		for(int row = 1; row <= graph.size(); row++)
			for(int line = 1; line <= graph.size(); line++) {
				solution[row][line] = line;	// initialize solution matrix
				cost[row][row] = 0;
			}

		for(int k = 1; k <= graph.size(); k++)
			for(int i = 1; i <= graph.size(); i++)
				for(int j = 1; j <= graph.size(); j++) {
					if(cost[i][k] != Integer.MAX_VALUE && cost[k][j] != Integer.MAX_VALUE &&
							cost[i][k] + cost[k][j] < cost[i][j]) {
						cost[i][j] = cost[i][k] + cost[k][j];
						solution[i][j] = solution[i][k];
					} 
				}

		int row = 1;
		int line = 1;
		while(row <= graph.size() && line <= graph.size()) {
			if(cost[row][line] < 0)
				return null;
			row++;
			line++;
		}
		return solution;
	}

	public LinkedList<GraphNode> topologicalSort() {
		if(!graph.isDirected())
			return null;

		LinkedList<GraphNode> solution = new LinkedList<>();
		Heap heap = new Heap(graph.size());
		for(int i = 1; i <= graph.size(); i++)
			heap.add(i, graph.getNode(i).inDeg());

		while(!heap.isEmpty()) {
			if(heap.getMinPriority() != 0) // if there are no source nodes(source node has inDeg == 0), that
				return null;				// means that there's a cycle

			int min = heap.extractMin();
			GraphNode minNode = graph.getNode(min);

			solution.add(minNode);
			for(GraphEdge edge : minNode.getAdjacentNodes()) {
				GraphNode adjacentNode = edge.getToNode();
				int newInDeg = heap.getNode(adjacentNode.id()).priority() - 1;
				heap.decreasePriority(adjacentNode.id(), newInDeg);
			}
		}
		return solution;
	}

	public LinkedList<GraphNode> limitedDepthSearch(int MAX_DEPTH) {
		LinkedList<GraphNode> list = new LinkedList<>();
		LinkedList<GraphNode> solution = new LinkedList<>();
		list.addFirst(graph.getNode(1));
		solution.add(graph.getNode(1));
		graph.getNode(1).visit();

		int currentDepth = 0;
		while(!list.isEmpty()) {
			GraphNode node = list.removeFirst();
			if(!solution.contains(node))
				solution.addLast(node);
			if(currentDepth < MAX_DEPTH) {
				for(GraphEdge cursor : node.getAdjacentNodes()) {
					if(!cursor.getToNode().wasVisited()) {
						cursor.getToNode().visit();
						list.addFirst(cursor.getToNode());
					}
				}
				currentDepth++;
			}
			else
				currentDepth = 0;
		}
		return solution;
	}

	public LinkedList<GraphNode> itDeepeningDepthSearch() {
		LinkedList<GraphNode> list = new LinkedList<>();
		LinkedList<GraphNode> solution = new LinkedList<>();

		return solution;
	}
}

