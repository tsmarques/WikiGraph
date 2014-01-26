package wiki.gui.graph;

public class NodeDrawLabel {
	private String text;

	public NodeDrawLabel(int nodeId, double posX, double posY) {
		setText(nodeId);

	}

	/**
	 * Set text of this label.
	 * It is the id shown inside
	 * the Node draw
	 * @param nodeId
	 */
	public void setText(int nodeId) {
		text = "" + (char)(64 + nodeId);
	}
	
	public String getText() {
		return text;
	}
}
