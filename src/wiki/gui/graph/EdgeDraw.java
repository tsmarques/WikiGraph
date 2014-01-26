package wiki.gui.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import wiki.util.GraphNode;

@SuppressWarnings("serial")
public class EdgeDraw extends QuadCurve2D.Double {	
	private static final int ARROW_LENGTH = 10;
	
	private final static int FIRST_QUADRANT = 1;
	private final static int SECOND_QUADRANT = 2;
	private final static int THIRD_QUADRANT = 3;
	private final static int FOURTH_QUADRANT = 4;
	private final static int BOTTOM = 0;			// if this edge is directed down
	private final static int LEFT = 5;				// if this edge is directed to the left
	private final static int TOP = 6;				// if this edge is directed up
	private final static int RIGHT = 7;				// if this edge is directed to the right
	
	private double maxX;
	private double maxY;
	private double minX;
	private double minY;
	
	private double startAngle;
	private double endAngle;
	
	
	private String nodeWeightLabel;
	private Rectangle2D.Double weightRec;
	private double labelX;
	private double labelY;
	private Path2D.Double arrowRIGHTTip;
	private Path2D.Double arrowLEFTTip;
	
	
	private boolean isCurved;
	
	
	public EdgeDraw() {
		nodeWeightLabel = null;
		weightRec = null;
		maxX = 0;
		maxY = 0;
		minX = 0;
		minY = 0;
		
		startAngle = 0;
		endAngle = 0;
		
		labelX = 0;
		labelY = 0;
	}
	
	public boolean isCurvded() {
		return isCurved;
	}
	
	public void draw(Graphics2D graphics, boolean isWeighted, boolean isDirected, Color color) {
		graphics.setColor(color);
		graphics.draw(this);
		if(isWeighted) {
			// weight label
			weightRec = new Rectangle2D.Double((int)getCenterX(),(int)getCenterY(), 15, 15);
			System.out.println("Edge Center X: " + getCenterX() + " Edge center Y: " + getCenterY());
			//graphics.fill(weightRec);
			graphics.setColor(Color.RED);
			graphics.drawString(getText(), (int)getLabelX(), (int)getLabelY());
			System.out.println("Label X: " + (int)getLabelX() + " Label Y: " + getLabelY());
			graphics.setColor(Color.BLACK);
		}
		else
			weightRec = null;
		if(isDirected) {
			graphics.setColor(color);
			graphics.draw(arrowRIGHTTip);
			graphics.draw(arrowLEFTTip);
		}
	}
	
	public void reDraw(Graphics2D graphics, GraphNode fromNode, GraphNode toNode, boolean isWeighted, boolean isDirected) {
		setPath(fromNode, toNode, isDirected);
		draw(graphics, isWeighted, isDirected,Color.DARK_GRAY);
	}
	
	public void erase(Graphics2D graphics) {
		graphics.setColor(Color.white);
		graphics.fill(this.getBounds2D());
		graphics.setColor(Color.black);
	}

	public void setPath(GraphNode fromNode, GraphNode toNode, boolean isDirected) {
		double startX = 0;
		double startY = 0;
		double endX = 0;
		double endY = 0;
		
		int startPos = getstartPos(fromNode.getCenterX(), fromNode.getCenterY(), 
				toNode.getCenterX(), toNode.getCenterY());
		int endPos = getEndPos(startPos);
		
		startAngle = getStartAngle(fromNode, toNode, startPos);
		endAngle = getEndAngle(startAngle);		
		
		startX = getX(fromNode, startPos, startAngle);
		startY = getY(fromNode, startPos, startAngle);
		
		endX = getX(toNode, endPos, endAngle);
		endY = getY(toNode, endPos, endAngle);
		
		
		setCurve(startX, startY, startX, startY, endX, endY);
		if(isDirected)
			setEdgeTip(endX, endY, endX > startX, endY > startY);
		//setWeightLabel(endX > startX);
		
		/** DEBUGGIN PURPOSES
		System.out.println("From Node: " + fromNode.id() + " to Node: " + toNode.id());
		System.out.println("Start pos: " + startPos + " end pos: " + endPos);
		System.out.println("Start Angle: " + Math.toDegrees(startAngle) + " end angle: " + Math.toDegrees(endAngle));
		System.out.println("Start X: " + startX + " start Y: " + startY);
		System.out.println("From node X: " + fromNode.getCenterX() + " from node Y: " + fromNode.getCenterY());
		System.out.println("End X: " + endX + " end Y: " + endY);
		System.out.println("To node X: " + toNode.getCenterX() + " to node Y: " + toNode.getCenterY());
		System.out.println("\n");**/
		
		
	}
	
	/**
	 * Set the arrow Head on the tip of the edge,
	 * @param posX
	 * @param posY
	 * @param RIGHTToLEFT
	 * @param upToTOP
	 */
	private void setEdgeTip(double posX, double posY, boolean RIGHTToLEFT, boolean upToTOP) {
		AffineTransform tx = new AffineTransform();
		arrowRIGHTTip = new Path2D.Double();
		arrowLEFTTip = new Path2D.Double();
		arrowRIGHTTip.moveTo(posX, posY);
		arrowLEFTTip.moveTo(posX, posY);
		
		double startX = 0;
		double startY = 0;
		
		if(RIGHTToLEFT)
			startX = posX - ARROW_LENGTH * Math.cos(Math.toRadians(0) - startAngle);
		
		else if(!RIGHTToLEFT)
			startX = posX + ARROW_LENGTH * Math.cos(Math.toRadians(0) - startAngle);
		
		if(upToTOP)
			startY = posY - ARROW_LENGTH * Math.sin(Math.toRadians(0) + startAngle);
		
		else if(!upToTOP)
			startY = posY + ARROW_LENGTH * Math.sin(Math.toRadians(0) + startAngle);
		
		
		arrowRIGHTTip.lineTo(startX, startY);
		arrowLEFTTip.lineTo(startX, startY);
		tx.rotate(Math.toRadians(30), posX, posY);
		arrowRIGHTTip.transform(tx);
		tx.rotate(Math.toRadians(300), posX, posY);
		arrowLEFTTip.transform(tx);
	}

	/**
	 * Returns the position of the point where the
	 * edge starts in the first Node.
	 * It can be, RIGHT, LEFT, BOTTOM, TOP or
	 * in the Quadrants from 1 to 4;
	 * @param fromX
	 * @param fromY
	 * @param toX
	 * @param toY
	 * @return
	 */
	private int getstartPos(double fromX, double fromY, double toX, double toY) {
		if(fromX == toX) { // vertical edge
			maxX = minX = fromX;
			if(fromY > toY) {
				maxY = fromY;
				minY = toY;
				return BOTTOM; // if the starting node of the edge is on TOP of the final one
			}
			else if(fromY < toY) {
				maxY = toY;
				minY = fromY;
				return TOP; // if the starting node of the edge is on BOTTOM of the final one
			}
		}
		else if(fromY == toY) { // horizontal edge
			maxY = minY = fromY;
			if(fromX > toX) {
				maxX = fromX;
				minX = toX;
				return RIGHT; // if the starting node is on the LEFT of the final one
			}
			else if(fromX < toX) {
				maxX = toX;
				minX = fromX;
				return LEFT; // if the starting node is on the RIGHT of the final one
			}
		}
		else { // if the nodes are not aligned in of the axis
			if(fromX < toX) { // left to right
				maxX = toX;
				minX = fromX;
				if(fromY > toY) { // first Quadrant; Up
					maxY = fromY;
					minY = toY;
					return FIRST_QUADRANT;
				}
				else if(fromY < toY) { // down
					maxY = toY;
					minY = fromY;
					return FOURTH_QUADRANT; // fourth Quadrant
				}
			}
			else if(fromX > toX) { // right to left
				maxX = fromX;
				minX = toX;
				if(fromY > toY) { // second Quadrant
					maxY = fromY;
					minY = toY;
					return SECOND_QUADRANT;
				}
				else if(fromY < toY) { // third Quadrant
					maxY = toY;
					minY = fromY;
					return THIRD_QUADRANT;
				}
			}
		}
		return -1;
	}

	private int getEndPos(int startPos) {
		if(startPos == BOTTOM)
			return TOP;
		if(startPos == TOP)
			return BOTTOM;
		if(startPos == RIGHT)
			return LEFT;
		if(startPos == LEFT)
			return RIGHT;
		
		// quadrants
		if(startPos == SECOND_QUADRANT)
			return FOURTH_QUADRANT;
		return Math.abs(3 - startPos) + 1;

	}

	private double getStartAngle(GraphNode fromNode, GraphNode toNode,int startPos) {
		if(startPos == BOTTOM)
			return Math.toRadians(90);
		if(startPos == TOP)
			return Math.toRadians(90);
		if(startPos == RIGHT)
			return Math.toRadians(0);
		if(startPos == LEFT)
			return Math.toRadians(0);
		
		double x = Math.abs(fromNode.getCenterX() - toNode.getCenterX());
		double y = Math.abs(fromNode.getCenterY() - toNode.getCenterY());
		

		return (Math.atan2(y, x)); // returns the angle between the two nodes
	}
	
	/**
	 * Returns the other angle in the triangle
	 * formed by the nodes with the edge
	 * connecting their centers and the edges
	 * with equation y = b and y = x
	 * being "b" the y coordinate of the center
	 * of one node and "x" the x coordinate of
	 * the center the other node.
	 * @param startAngle
	 * @return
	 */
	private double getEndAngle(double startAngle) {
		if(startAngle == 0)
			return Math.abs(Math.toRadians(0));
		if(startAngle == Math.toRadians(270) || startAngle == Math.toRadians(90))
			return 0;
		return Math.abs(Math.toRadians(90) - startAngle);
	}
	
	private double getX(GraphNode node, int pos, double angle) {
		double x = 0;
		
		if(angle == startAngle)
			x = Math.abs(Math.cos(angle) * GraphNode.RADIUS);
		else if(angle == endAngle)
			x = Math.abs(Math.sin(angle) * GraphNode.RADIUS);
		
		if(pos == FIRST_QUADRANT || pos == BOTTOM) // for BOTTOM or TOP x = 0, x is given by the expressions above 
			return node.getCenterX() + x;
		if(pos == SECOND_QUADRANT || pos == RIGHT)
			return node.getCenterX() - x;
		if(pos == THIRD_QUADRANT || pos == TOP)
			return node.getCenterX() - x;
		if(pos == FOURTH_QUADRANT || pos == LEFT)
			return node.getCenterX() + x;
		return 0;
	}
	
	private double getY(GraphNode node, int pos, double angle) {
		double y = 0;
		
		if(angle == startAngle)
			y = Math.abs(Math.sin(angle) * GraphNode.RADIUS);
		else if(angle == endAngle)
			y = Math.abs(Math.cos(angle) * GraphNode.RADIUS);
		
		if(pos == FIRST_QUADRANT || pos == BOTTOM)
			return node.getCenterY() - y;
		else if(pos == SECOND_QUADRANT || pos == RIGHT) // for RIGHT or LEFT y = 0
			return node.getCenterY() - y;
		else if(pos == THIRD_QUADRANT || pos == TOP)
			return node.getCenterY() + y;
		else if(pos == FOURTH_QUADRANT || pos == LEFT)
			return node.getCenterY() + y;
		return 0;
	}
	
	public void setLabelText(int weight) {
		nodeWeightLabel = "" + weight;
	}
	
	public String getText() {
		return nodeWeightLabel;
	}
	
	public Rectangle2D.Double getLabelRectangle() {
		return weightRec;
	}
	
	public double getLabelX() {
		return labelX = weightRec.getCenterX();// - 2.5;
	}
	
	public double getLabelY() {
		return labelY = weightRec.getCenterY() + 4;
	}
	
	public double minX() {
		return minX;
	}
	
	public double maxX() {
		return maxX;
	}
	
	public double minY() {
		return minY;
	}
	
	public double maxY() {
		return maxY;
	}
	
	/**
	 * Get the X position in
	 * the center of this Edge.
	 * @return
	 */
	private double getCenterX() {
		System.out.println("MaxX: " + maxX + " MinX: " + minX);
		return maxX - ((maxX - minX) / 2);
	}
	
	private double getCenterY() {
		return maxY - ((maxY - minY) / 2);
	}
}
