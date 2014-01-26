package wiki.tests;

import java.util.LinkedList;
import java.util.Scanner;

import wiki.util.GraphEdge;


public class Tests {
	Scanner input;
	public Tests() {
		input = new Scanner(System.in);
	}

	public void Graph() {
		TestGraph test = new TestGraph();
		test.buildGraph();

		System.out.println("0.Print all nodes\n1.Print all edges\n2.Print edges from a given node\n3.Add edge\n4.Remove edge" +
				"\n5.Add node\n6.Remove node\n7.Is connected\n8.Graph size\n\n10.Breadth-First search(BFS)\n11.Depth-First search\n" +
				"12.Prim's algorithm\n13.Kruskal's algorithm\n14.Dijkstra's algorithm");

		int choice = 0;
		choice = input.nextInt();
		while(choice != -1) {
			//print all nodes
			if(choice == 0) // working
				test.printAllNodes();

			// print all edges
			else if(choice == 1) // working
				test.printAllEdges();

			// print node edges
			else if(choice == 2) { // working
				System.out.println("Choose node: ");
				int node = input.nextInt();
				test.printNodeEdges(node);
			}
			//add edge
			else if(choice == 3) {
				System.out.print("Add edge from: "); // working
				int fromNode = input.nextInt();

				System.out.print("\nTo: ");
				int toNode = input.nextInt();
				System.out.println();
				boolean succ;

				if(!test.graph.isWeighted())
					succ = test.testAddEdgeToNode(fromNode, toNode,1);
				else {
					System.out.print("Weight: ");
					int weight = input.nextInt();
					System.out.println();
					succ = test.testAddEdgeToNode(fromNode, toNode,weight);
				}

				if(!succ)
					System.out.println("Edge already exists");
				else
					System.out.println("Success");
			}
			// remove edge
			else if(choice == 4) { // working
				System.out.print("Remove edge from: ");
				int fromNode = input.nextInt();
				System.out.print("\nTo: ");
				int toNode = input.nextInt();
				System.out.println();
				boolean succ = test.testRemoveEdgeFromNode(fromNode, toNode);

				if(!succ)
					System.out.println("Edge does not exist");
				else
					System.out.println("Success");
			}
			// add node
			else if(choice == 5) { // working
				boolean succ = test.testAddNode();

				if(!succ)
					System.out.println("Graph is full");
				else
					System.out.println("Success");
			}
			// remove node
			else if(choice == 6) {
				String allNodes;
				System.out.println("All Nodes?(y/n)");
				allNodes = input.next();

				if(allNodes.equals("y")) {
					int graphSize = test.graph.size();
					for(int i = 1; i <= graphSize; i++)
						test.graph.removeNode(1);
				}
				else {
					System.out.print("Node to remove:");
					int node = input.nextInt();
					boolean succ = test.testRemoveNode(node);

					System.out.println();
					if(!succ)
						System.out.println("Node does not exist");
					else
						System.out.println("success");
				}
			}
			else if(choice == 7) {
				boolean succ = test.testIsConnected();
				if(succ)
					System.out.println("Is connected");
				else
					System.out.println("Not connected");
			}

			else if(choice == 8)
				System.out.println("Size: " + test.testSize());

			//Apply Breadth-First search
			else if(choice == 10)
				test.testBFS();

			// Apply Depth-First search
			else if(choice == 11)
				test.testDFS();

			// Apply Prims algorithm
			else if(choice == 12) {
				boolean succ = false;
				boolean notDirected = true;
				boolean isConnected = true;
				boolean isWeighted = true;

				if(test.graph.isDirected()) {
					notDirected = false;
					System.out.println("ERROR: Graph is directed");
				}

				if(!test.testIsConnected()) {
					isConnected = false;
					System.out.println("ERROR: Graph is not connected");
				}

				if(!test.graph.isWeighted()) {
					isWeighted = false;
					System.out.println("ERROR: Graph is not weighted");
				}

				succ = notDirected && isConnected && isWeighted;
				if(succ) {
					LinkedList<GraphEdge> solution = test.testPrim();
					for(GraphEdge edge : solution) {
						System.out.println(edge.getFromNode().id() + " -> " + edge.getToNode().id());
					}
				}
			}
			//Kruskal
			else if(choice == 13) 
				System.out.println("Kruskal's algorithm not implemented yet");

			//Dijkstra
			else if(choice == 14) {
				LinkedList<int[]> solution = test.testDijkstra();
				if(solution == null)
					System.out.println("ERRROR: graph has negative edges");
				else {
					int[] predecessors = solution.removeFirst();
					int[] cost = solution.removeFirst();
					System.out.println("Predecssors: ");
					for(int i = 1; i < predecessors.length; i++)
						System.out.print(" " + predecessors[i]);
					System.out.println("\nCosts: ");
					for(int i = 1; i < cost.length; i++) {
						if(cost[i] != Integer.MAX_VALUE)
							System.out.print(" " + cost[i]);
						else
							System.out.print(" inf");
					}
					System.out.println();
				}
			}	
			choice = input.nextInt();
		}
	}

	public void Heap() {
		TestHeap test = new TestHeap(6);
		System.out.println("0.Build heap\n1.Print heap\n2.Add new node\n3.Extract min node\n4.Get size" +
				"\n5.Is empty\n6.Is full\n\n7.Decrease priority");

		int choice = 0;
		int currentNodeId = 6;
		choice = input.nextInt();
		while(choice != -1) {
			if(choice == 0)
				test.buildHeap();
			else if(choice == 1)
				test.printHeap();

			else if(choice == 2) {
				System.out.println("Current node Id: " + currentNodeId);
				System.out.print("Node id: ");
				int id = input.nextInt();
				System.out.println();
				if(id > currentNodeId)
					currentNodeId++;

				System.out.print("Priority: ");
				int priority = input.nextInt();
				System.out.println();
				int succ = test.testAddNode(id, priority);

				if(succ == 1)
					System.out.println("Success");
				else if(succ == 0)
					System.out.println("Heap is full");
				else if(succ == -1)
					System.out.println("Node already exists");
			}
			else if(choice == 3) {
				int min = test.testeExtractMin();
				if(min == -1)
					System.out.println("Heap is empty");
				else
					System.out.println("Min node: " + min);
			}
			else if(choice == 4)
				System.out.println("Heap size: " + test.testGetSize());
			else if(choice == 5) {
				boolean isEmpty = test.testIsEmpty();
				if(isEmpty)
					System.out.println("Heap is empty");
				else
					System.out.println("Heap is not empty");
			}
			else if(choice == 6) {
				boolean isFull = test.testIsFull();
				if(isFull)
					System.out.println("Heap is full");
				else
					System.out.println("Heap is not full");
			}

			else if(choice == 7) {
				System.out.print("Node to decrease priority: ");
				int id = input.nextInt();
				System.out.println();
				System.out.println("Priority: ");
				int priority = input.nextInt();
				boolean succ =  test.testDecreasePriority(id, priority);
				if(succ)
					System.out.println("Success");
				else
					System.out.println("Node does not exist");
			}
			choice = input.nextInt();
		}
	}
}
