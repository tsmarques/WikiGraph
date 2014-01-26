package wiki.gui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class NodeDraw extends Ellipse2D.Double {
	public final static int RADIUS = 20;
	private static final int NODE_WIDTH = 40;
	private static final int NODE_HEIGHT = 40;
	
	private final Stroke thickStroke;
	
	private int id;
	private NodeDrawLabel idLabel;
	private double posX;
	private double posY;
	
	public NodeDraw() {
		idLabel = null;
		id = 0;
		posX = 0;
		posY = 0;
		thickStroke = new BasicStroke(3);
	}
	
	public void setNodeInfo(int labelText, double posX, double posY) {
		id = labelText;
		this.posX = posX;
		this.posY = posY; 
		setIdLabel(labelText);
	}
	
	public void draw(Graphics2D graphics, double posX, double posY, Color color) {
		graphics.setColor(color);
		graphics.draw(this);
		// draw node Id Label
		graphics.setColor(Color.DARK_GRAY);
		graphics.drawString(this.label().getText(), (float)this.getCenterX() - 3, (float)this.getCenterY() + 4);
		graphics.setColor(Color.black);
	}
	
	/**
	 * Repaints the given node.
	 * If posX and posY are equal to
	 * -1, that means the node will be
	 * drawn in the same place, otherwise
	 * they will drawn in the position given
	 * by their values.
	 * @param node
	 * @param posX
	 * @param posY
	 */
	public void reDraw(Graphics2D graphics, double posX, double posY) {
		if(posX != -1 && posY != -1) // if the node is moving
			setNodeInfo(id, posX, posY);
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.setStroke(thickStroke);
		draw(graphics, posX, posY, new Color(0, 128, 128));
	}
	
	/**
	 * Erases the drawing this node.	
	 * @param graphics
	 */
	public void erase(Graphics2D graphics) {
		graphics.setColor(Color.white);
		graphics.fillRect((int)this.getMinX() - 1, (int)this.getMinY() - 1, 49, 49);
		graphics.setColor(Color.black);
	}
	
	/**
	 * Handles this node's
	 * and respective label dragging.
	 * @param graphics
	 * @param posX
	 * @param posY
	 */
	public void drag(Graphics2D graphics, double posX, double posY) {	
		erase(graphics);
		reDraw(graphics, posX, posY);
		//repaintGraph();
	}
	
	/**
	 * Sets the id that will be draw.
	 * @param labelText
	 */
	private void setIdLabel(int labelText) {
		idLabel = new NodeDrawLabel(labelText, getCenterX(), getCenterY());
	}
	
	/**
	 * Returns the label object
	 * of this node
	 * @return
	 */
	public NodeDrawLabel label() {
		return idLabel;
	}
	
	public void changeDrawId(int id) {
		this.id = id;
		idLabel.setText(id);
	}
	
	@Override
	public Rectangle2D getBounds2D() {
		return null;
	}

	@Override
	public double getHeight() {
		return NODE_HEIGHT;
	}

	@Override
	public double getWidth() {
		return NODE_WIDTH;
	}

	@Override
	public double getX() {
		return posX - NODE_WIDTH / 2;
	}

	@Override
	public double getY() {
		return posY - NODE_HEIGHT / 2;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFrame(double arg0, double arg1, double arg2, double arg3) {
		// TODO Auto-generated method stub
		
	}

}
