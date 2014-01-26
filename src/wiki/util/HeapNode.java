package wiki.util;
/************************
 * Implementation of the
 * Nodes used in the queue		
 * @author tmarques
 ************************/
public class HeapNode {
	private int id; // used in Prim algorithm
	private GraphEdge edge; // used in kruskal's algorithm
	private int priority;
	
	HeapNode(int id, int priority) {
		setId(id);
		setPriority(priority);
	}
	
	HeapNode(GraphEdge edge, int id, int priority) {
		setEdge(edge);
		setId(id);
		setPriority(priority);
	}
	
	public int id() {
		return id;
	}
	
	public GraphEdge edge() {
		return edge;
	}
	
	public int priority(){
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
		
	private void setId(int id) {
		this.id = id;
	}
	
	private void setEdge(GraphEdge edge) {
		this.edge = edge;
	}
}