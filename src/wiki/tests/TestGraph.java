package wiki.tests;

import java.util.LinkedList;
import java.util.Scanner;

import wiki.util.Graph;
import wiki.util.GraphEdge;

/**
 * Class to test Directed Graph
 * and its methods
 * [Tested]
 * @author tmarques
 *
 */
public class TestGraph {
	Graph graph;
	Scanner input;
	
	public TestGraph() {
		input = new Scanner(System.in);
		initializeGraph();
	}
	
	private void initializeGraph() {
		System.out.print("Is the graph directed: ");
		boolean isDirected = input.nextBoolean();
		System.out.println();
		System.out.print("Is the graph weighted: ");
		boolean isWeighted = input.nextBoolean();
		System.out.println();
		
		graph = new Graph(isDirected,isWeighted);
	}
	
	/**
	 * Builds the graph used in
	 * the unit tests.
	 */
	public void buildGraph() {
		for(int i = 1; i <= 8; i++)
			graph.addNode();
		
		testAddEdgeToNode(1, 2, 10);
		testAddEdgeToNode(1, 3, 5);
		testAddEdgeToNode(1, 6, 5);
		testAddEdgeToNode(3, 2, 4);
		testAddEdgeToNode(3, 4, 3);
		testAddEdgeToNode(4, 2, 2);
		testAddEdgeToNode(5, 3, 1);
		testAddEdgeToNode(6, 4, 1);
		testAddEdgeToNode(6, 8, 2);
		testAddEdgeToNode(7, 6, 2);
		testAddEdgeToNode(7, 4, 4);
		testAddEdgeToNode(7, 8, 3);
		testAddEdgeToNode(8, 5, 3);
	}
	
	/**
	 * Add node to the graph
	 * @return
	 */
	public boolean testAddNode() {
		return graph.addNode();
	}
	
	/**
	 * Remove given node
	 * @param node
	 * @return
	 */
	public boolean testRemoveNode(int node) {
		return graph.removeNode(node);
	}
	
	/**
	 * Link two nodes: 
	 * fromNode to toNode
	 * @param index
	 * @return
	 */
	public boolean testAddEdgeToNode(int fromNode,int toNode,int weight) {
		return graph.addEdge(fromNode, toNode, weight);
	}
	
	/**
	 * Remove given edge.
	 * @param fromNode
	 * @param toNode
	 * @return
	 */
	public boolean testRemoveEdgeFromNode(int fromNode, int toNode) {
		return graph.removeEdge(fromNode, toNode);
	}
	
	/**
	 * Print all nodes used by the graph
	 */
	public void printAllNodes() {
		for(int i = 1; i <= graph.size(); i++) {
			System.out.println("Node: " + graph.getNode(i).id() + " In degree: " + graph.getNode(i).inDeg() + " Out degree: " + graph.getNode(i).outDeg());
		}
	}
	/**
	 * Print all edges coming from
	 * all the used nodes
	 */
	public void printAllEdges() {
		for(int i = 1; i <= graph.size(); i++)
			printNodeEdges(i);
	}
	
	/**
	 * Prints all edges coming from the given Node
	 * @param fromNode
	 */
	public void printNodeEdges(int fromNode) {
		System.out.println("#Node: " + graph.getNode(fromNode).id());
		for(GraphEdge cursor : graph.getNode(fromNode).getAdjacentNodes()) {
			if(!graph.isWeighted())
				System.out.println("-> " + cursor.getToNode().id());
			else
				System.out.println("-> " + cursor.getToNode().id() + "Weight: " + cursor.weight());
		}
	}
	
	public boolean testIsConnected() {
		return graph.isConnected().size() == graph.size();
	}
	
	public int testSize() {
		return graph.size();
	}
	
	/*****************
	 * Test Algorithms
	 *****************/
	
	public void testBFS() {
		graph.breadthFirstSearch();
	}
	
	public void testDFS() {
		graph.depthFirstSearch();
	}
	
	public LinkedList<GraphEdge> testPrim() {
		return graph.primAlgorithm();		
	}
	
	public void testKruskal() {
		
	}
	
	public LinkedList<int[]> testDijkstra() {
		if(graph.hasNegativeEdges())
			return null;
		return graph.Dijkstra();
	}
	
}
	