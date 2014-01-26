package wiki.util;

public class Heap {
	private final int MAX_NODES = 20; // maximum capacity of the Heap
	HeapNode[] nodes; // Heap nodes
	int[] pos; // nodes position in the Heap
	int nElem; // number of elements currently on the Heap

	public Heap(int size) {
		nodes = new HeapNode[MAX_NODES + 1];
		pos = new int[MAX_NODES + 1];
		nElem = 0;
	}
	
	/**
	 * Print the heap like an array
	 */
	public void printHeap() {
		for(int i = 1; i <= nElem; i++) {
			System.out.println("Valor: " + nodes[i].id() + " Prio: " + nodes[i].priority() + " Pos: " + pos[nodes[i].id()]);
		}
	}
	
	/**
	 * Returns the min element on the heap
	 * without removing it.For that
	 * use extractMin().
	 * @return
	 */
	public HeapNode getFirst() {
		return nodes[1];
	}
	
	/**
	 * Returns the HeapNode that
	 * holds the given node
	 * @param id
	 * @return
	 */
	public HeapNode getNode(int id) {
		return nodes[pos[id]];
	}
	
	/**
	 * Returns true if the Heap
	 * is empty
	 * @return
	 */
	public boolean isEmpty() {
		return nElem == 0;
	}
	
	/**
	 * Returns true if the
	 * number nodes is equal
	 * to the maximum capacity : MAX_NODES
	 * @return
	 */
	public boolean isFull() {
		return nElem == MAX_NODES;
	}
	
	/**
	 * Returns the maximum capacity
	 * of the heap
	 * @return
	 */
	public int maxSize() {
		return MAX_NODES;
	}

	/**
	 * Returns <code>-1</code> if the node is already in the
	 * queue, <code>0</code> if the queue is full and <code>1</code>
	 * if a success.
	 * @param id
	 * @param priority
	 * @return
	 */
	public int add(int id, int priority) {
		if(pos[id] != 0) // node already exists
			return -1;
		if(isFull()) // queue is full
			return 0;

		nElem++;
		nodes[nElem] = new HeapNode(id, priority);
		pos[id] = nElem;
		for(int i = nElem / 2; i >= 1; i--)
			heapify(i);

		return 1;
	}
	
	/**
	 * Adds a node to the heap,
	 * with an edge.
	 * Used in Kruskal's Algorithm
	 * @param edge
	 * @param id
	 * @param priority
	 * @return
	 */
	public int addEdge(GraphEdge edge, int id, int priority) {
		if(pos[id] != 0) // node already exists
			return -1;
		if(isFull()) // queue is full
			return 0;

		nElem++;
		nodes[nElem] = new HeapNode(edge, id, priority);
		pos[id] = nElem;
		for(int i = nElem / 2; i >= 1; i--)
			heapify(i);

		return 1;
	}
	
	/**
	 * Returns and remove the id of the node
	 * with the lowest priority.
	 * Used in Prim and Dijkstra's Algorithm
	 * @return
	 */
	public int extractMin() {
		int min = nodes[1].id();
		swap(1,nElem);
		pos[nodes[nElem].id()] = 0;
		nElem--;
		heapify(1);

		return min;
	}
	
	/**
	 * Returns the priority of the
	 * root of this Heap, that is
	 * the minimum priority.
	 * Only used on Topological Sorting
	 * @return
	 */
	public int getMinPriority() {
		return nodes[1].priority();
	}
	
	/**
	 * Returns and remove the edge with
	 * the smallest weight.
	 * Used in Kruskal's Algorithm
	 * @return
	 */
	public GraphEdge extractMinEdge() {
		GraphEdge min = nodes[1].edge();
		swap(1,nElem);
		pos[nodes[nElem].id()] = 0;
		nElem--;
		heapify(1);

		return min;
	}

	/**
	 * Decrease the priority of a given node
	 * to the given value.
	 * Used in Prim and Dijkstra's algorithm
	 * @param id
	 * @param priority
	 */
	public boolean decreasePriority(int id, int priority) {
		if(pos[id] == 0) // if node doesn't exist
			return false;
		int index = pos[id];
		nodes[index].setPriority(priority);

		while(index > 1 && compare(index,index / 2) < 0) {
			swap(index, index / 2);
			index = index / 2;
		}
		
		return true;
	}
	
	/**
	 * Given the position of two nodes
	 * switch them.
	 * @param pos1
	 * @param pos2
	 */
	private void swap(int pos1, int pos2) {
		HeapNode temp = nodes[pos1];
		pos[temp.id()] = pos2;
		pos[nodes[pos2].id()] = pos1;
		nodes[pos1] = nodes[pos2];
		nodes[pos2] = temp;		
	}
	
	/**
	 * Given the position of two nodes in the
	 * tree, compare their priorities.
	 * @param pos1
	 * @param pos2
	 * @return
	 */
	private int compare(int pos1, int pos2) {
		if(nodes[pos1].priority() < nodes[pos2].priority())
			return -1;
		if(nodes[pos1].priority() > nodes[pos2].priority())
			return 0;
		return 1;
	}	
	
	/**
	 * Mantains the Heap property.
	 * @param node
	 */
	private void heapify(int index) { // O(log n)
		int left = 2*index;
		int right = 2*index + 1;
		int min = index;

		if(left <= nElem && compare(left,min) < 0) 
			min = left;
		if(right <= nElem && compare(right,min) < 0)  
			min = right;

		if(min != index) {
			swap(min,index);
			heapify(min);
		}
	}
}
