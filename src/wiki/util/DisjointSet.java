package wiki.util;

public class DisjointSet {
	private int[] set;
	private int size;
	
	public DisjointSet(int nNodes) {
		set = new int[nNodes + 1];
		size = nNodes + 1;
		initializeSet();
	}
	
	private void initializeSet() {
		for(int i = 0; i < size; i++)
			set[i] = 0;
	}
	
	public int find(int x) {
		int setNumber = x;
		while(set[x] != 0) {
			setNumber = set[x];
			x = set[x];
		}
		return setNumber;
	}
	
	public void union(int node1, int node2) {
		int root1 = find(node1);
		int root2 = find(node2);
		
		if(root1 != root2)
			set[root2] = root1;
	}
}
