package wiki.gui;

import wiki.gui.graph.NodeDraw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import wiki.util.Graph;
import wiki.util.GraphEdge;
import wiki.util.GraphNode;

@SuppressWarnings("serial")
public class Canvas extends JPanel implements MouseInputListener {
	private final TextStrings strings;

	private Graphics2D graphics;
	private Graphics2D buffGraphics;
	private BufferedImage offScreen;
	private final RenderingHints rh;
	private final Stroke stdStroke;
	private final Stroke thickStroke;

	private ControlPanel cPanelPointer;
	private DrawingTools dToolsPointer;

	private Graph graph;
	private GraphNode fromNode;				// used to draw an edge
	private boolean algorithmActive;		// used to know if there is an algorithm being applied
	private boolean dragging;				// if a node is being dragged

	/* auxiliary nodes */
	private GraphNode startNode; 			// used for Shortest Path Algorithms
	private GraphNode finalNode;			// When using shortest path algorithms the user selects a node 
	//to where he wants the shortest path to, and it is saved here
	private GraphNode draggedNode;			// node being dragged

	/* Algorithm Solutions */
	private int[] dijkstraSolution; 		// stores the solution of Dijkstra's algorithm
	private int[] bellmanFordSolution;		// stores the solution of BellManFord's algorithm
	private int[][] floydWarshallSolution;	// stores the solution of Bellman-Ford's algorithm

	/* Time and memory variables */
	private long startTime;					// time that an algorithm starts executing
	private long elapsedTime;				// time it took for the algorithm to execute in milliseconds
	private float finalTime;				// elapsedTime in seconds
	private Runtime runtime;				// used to get memory usage
	private long usedMemory;				// used memory to run a certain algorithm


	public Canvas(TextStrings strings) {
		this.strings = strings;
		rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		offScreen = new BufferedImage(1024, 1024,BufferedImage.TYPE_INT_ARGB);
		buffGraphics = (Graphics2D)offScreen.getGraphics();
		stdStroke = new BasicStroke(2);
		thickStroke = new BasicStroke(3);

		addMouseListener(this);
		addMouseMotionListener(this);

		fromNode = null;
		finalNode = null;
		draggedNode = null;
		algorithmActive = false;

		startTime = 0;
		elapsedTime = 0;
		finalTime = 0;
		runtime = null;
		usedMemory = 0;

		setPanelProperties();
		setGraphicProperties();
	}

	public void setPanelPointers(ControlPanel cPanelPointer, DrawingTools dToolsPointer) {
		this.cPanelPointer = cPanelPointer;
		this.dToolsPointer = dToolsPointer;
	}

	public void newGraph(boolean isDirected, boolean isWeighted) {
		graph = new Graph(isDirected, isWeighted);
	}

	/**
	 * Deletes the graph in use and
	 * cleans canvas.
	 */
	public boolean reset() {
		if(algorithmActive == false) {
			graph = null;
			clearCanvas();
			graphics.drawImage(offScreen, 0, 0, this);
			return true;
		}
		algorithmActive = false;
		fromNode = null;
		startNode = null;
		finalNode = null;
		draggedNode = null;
		dijkstraSolution = null;

		unLockPanel();
		repaintGraph();
		return false;
	}

	public void setGraphWeighted(boolean isWeighted) {
		graph.setGraphWeighted(isWeighted);
		repaintGraph();
	}

	private void setGraphicProperties() {
		rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		//rh.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		//setDoubleBuffered(true);
	}

	private void setPanelProperties() {
		setBackground(Color.WHITE);
	}

	/**
	 * This method is called when
	 * the window is maximized/minimized
	 * and there's the need to redraw the graph
	 */
	private void repaintGraph() {
		clearCanvas();
		buffGraphics.setStroke(thickStroke);
		for(GraphNode cursor : graph.getNodes())
			cursor.reDraw(buffGraphics, cursor.getCenterX(), cursor.getCenterY());

		for(GraphEdge edge : graph.getEdges()) {
			buffGraphics.setStroke(stdStroke);
			buffGraphics.setColor(Color.black);
			edge.reDraw(buffGraphics,edge.getFromNode(), edge.getToNode(), graph.isWeighted(), graph.isDirected());
		}
		graphics.drawImage(offScreen,0 ,0, this);
	}

	private void clearCanvas() {
		buffGraphics.setColor(Color.WHITE);
		buffGraphics.fillRect(0, 0, getWidth(), getHeight());
		buffGraphics.setColor(Color.BLACK);
	}

	/**
	 * Draws a node and adds him(the object) to the graph
	 * and the nodesListraw list.
	 * @param posX
	 * @param posY
	 */
	private void drawNode(double posX, double posY) {
		// adding the node to the graph
		graph.addNode();

		// get the new node to draw
		buffGraphics.setStroke(thickStroke);
		GraphNode newNode = graph.getNode(graph.size());
		newNode.setNodeInfo(newNode.id(), posX, posY);
		newNode.draw(buffGraphics,posX, posY, new Color(0, 128, 128));

		buffGraphics.setStroke(stdStroke);
		repaintGraph();
		//graphics.drawImage(offScreen,0,0,this);
	}

	/**
	 * Returns the node that is in the
	 * coordinates, where the mouse clicked.
	 * If there's no node there it returns null.
	 * @param touchX
	 * @param touchY
	 * @return
	 */
	private GraphNode getTouchedNode(double touchX, double touchY) {
		for(GraphNode cursor : graph.getNodes())
			if(cursor.contains(touchX, touchY))
				return cursor;
		return null;
	}

	/**
	 * Erases the given node, by calling 
	 * eraseNode method, and removes it
	 * from the graph.
	 * @param touchX
	 * @param touchY
	 */
	private void eraseAndRemoveNode(double touchX, double touchY) {
		GraphNode cursor = getTouchedNode(touchX, touchY);
		if(cursor != null) {
			//cursor.erase(buffGraphics);
			int fromNode = cursor.id();

			// remove node from the graph
			graph.removeNode(cursor.id());
			repaintlabels(fromNode); // repaint labels from this node to front
			repaintGraph();
		}
	}

	/**
	 * Repaints the labels from all the nodes
	 * starting in the given node index.
	 * @param fromNode
	 */
	private void repaintlabels(int fromNode) {
		for(int i = fromNode; i <= graph.size(); i++) {
			GraphNode node = graph.getNode(i);
			float posX = (float)node.getCenterX() - 3;
			float posY = (float)node.getCenterY() + 4;

			// clear current node Id
			buffGraphics.setColor(getBackground());
			buffGraphics.fillRect((int)posX, (int)node.getCenterY() - 5, 10, 10);
			buffGraphics.setColor(Color.black);

			node.changeDrawId(i);
			buffGraphics.setColor(Color.red);
			buffGraphics.drawString(node.label().getText(), posX, posY);
		}
	}

	private void drawEdge(GraphNode toNode) {
		boolean success = graph.addEdge(fromNode.id(), toNode.id(), 1);
		if(success) { // if successfully added an edge to the graph
			GraphEdge newEdge = fromNode.getEdge(toNode); // get the newly added edge from the graph
			newEdge.setPath(fromNode, toNode, graph.isDirected()); // set edge nodes
			newEdge.setLabelText(newEdge.weight()); // set edge weight

			buffGraphics.setStroke(stdStroke);
			newEdge.draw(buffGraphics, graph.isWeighted(), graph.isDirected(), Color.DARK_GRAY); // drawing the edge
			graphics.drawImage(offScreen,0, 0, this);

			fromNode = null;
			toNode = null;
		}
		else // if the edge already exists
			JOptionPane.showMessageDialog(this, strings.getText("ERROR_EDGE_EXISTS"), 
					strings.getText("ERROR_LABEL"), JOptionPane.ERROR_MESSAGE);
	}

	private void eraseAndRemoveEdge(GraphEdge edge) {
		if(edge != null)
			graph.removeEdge(edge.getFromNode().id(), edge.getToNode().id());
		repaintGraph();
		fromNode = null;
	}

	/**
	 * Checks if a node is being drawn on top, or
	 * too close to another(less than 12.5 pixels),
	 * or (partially or completely) outside Canvas bounds
	 * Returns true if it isn't, allowing
	 * the node to be drawn.
	 * @param posX
	 * @param posY
	 * @return
	 */
	private boolean isSafeToDraw(double posX, double posY) { // working
		for(GraphNode cursor : graph.getNodes())
			if(computeRealDistance(cursor, posX, posY) < 12.5)
				return false;

		/*if(graph.getEdges().size() == 0) // if there are no edges yet
			return true;*/

		GraphNode buffer = new GraphNode(0); // tmp to make the following test
		buffer.setNodeInfo(0, posX, posY);

		/*for(GraphEdge cursor : graph.getEdges()) // if the node is being drawn on top of an edge
			if(cursor.intersects(buffer.getMinX(), buffer.getMinY(), buffer.getMaxX() - buffer.getMinX(), 
					buffer.getMaxY() - buffer.getMinY()))
				return false;*/

		if(buffer.getMinX() < 0 || buffer.getMaxX() > getWidth())
			return false;
		if(buffer.getMinY() < 0 || buffer.getMaxY() > getHeight())
			return false;
		return true;
	}

	/**
	 * Checks if a node is being dragged on top or
	 * too close to another(less than 12.5 pixels)
	 * or (partially or completely) outside Canvas bounds
	 * @param posX
	 * @param posY
	 * @return
	 */
	private boolean isSafeToDrag(GraphNode node, double posX, double posY) {
		if(posX - node.RADIUS < 0 || posX + node.RADIUS > getWidth())
			return false;
		if(posY - node.RADIUS < 0 || posY + node.RADIUS > getHeight())
			return false;

		if(graph.size() == 1) // if there's only one node it is safe to drag
			return true;

		for(GraphNode cursor : graph.getNodes()) {
			if(cursor != node) // not to compare with the same node to drag
				if(computeRealDistance(cursor, posX, posY) < 12.5)
					return false;
		}
		return true;
	}

	/**
	 * Computes the distance from a position
	 * in the drawing area to the other nodes.
	 * It is used to see if it's safe to drag
	 * a node to a certain position or to be drawn
	 * in said position, on the methods:
	 * isSafeToDraw and isSafeToDrag.
	 * 
	 * @param node: Node to be drawn or dragged
	 * @param posX : x coordinate of the place to be
	 * drawn or dragged to
	 * @param posY : y coordinate of the place to be
	 * drawn or dragged to.
	 * @return the real distance
	 */
	private double computeRealDistance(GraphNode node, double posX, double posY) {
		double z = 0; // distance from center to center
		double diffX = 0;
		double diffY = 0;

		diffX = Math.abs(posX - node.getCenterX());
		diffY = Math.abs(posY - node.getCenterY());

		if(diffX == 0)
			z = diffY;
		else if(diffY == 0)
			z = diffX;
		else {
			double x = diffX * diffX;
			double y = diffY * diffY;

			z = Math.sqrt(x + y);
		}

		return z - (2 * NodeDraw.RADIUS);	// distance without the radius of the nodes
	}

	private void lockPanel() {
		MainWindow.algorithmSelection(false);
		cPanelPointer.disableAlgorithmSelection();
	}

	private void unLockPanel() {
		MainWindow.algorithmSelection(true);
		cPanelPointer.enableAlgorithmSelection();
	}

	public void applyAlgorithm(int algorithm) {
		if(graph.isEmpty())
			JOptionPane.showMessageDialog(this, strings.getText("ERROR_GRAPH_EMPTY"), 
					strings.getText("ERROR_LABEL"), JOptionPane.ERROR_MESSAGE);
		else {
			if(algorithm == cPanelPointer.T_BFS) {
				algorithmActive = true;
				lockPanel();
				applyBreadthSearch();
			}
			else if(algorithm == cPanelPointer.T_DFS) {
				algorithmActive = true;
				lockPanel();
				applyDepthSearch();
			}

			else if(algorithm == cPanelPointer.T_LIMITED_DFS) {
				String userInput = JOptionPane.showInputDialog(null, strings.getText("MAX_DEPTH"), 1);
				if(userInput != null) {
					if(userInput.matches("-?\\d+")) { // type numeric
						int MAX_DEPTH = parseUserInput(userInput);
						lockPanel();
						applyLimitedDepthSearch(MAX_DEPTH);	
					}				
				}
			}

			else if(algorithm == cPanelPointer.G_IS_DAG) {
				lockPanel();
				algorithmActive = true;

			}
			else if(algorithm == cPanelPointer.G_IS_CONNECTED) {
				lockPanel();
				algorithmActive = true;
				applyIsConnectedAlgorithm();
			}

			/* If there are no edges in the graph
			there's no point in computing shortest path
			or Minimum Spanning Tree algorithms,
			or Topological Ordering,
			so if that's the case, this method
			finishes here */
			else if(graph.getEdges().size() == 0) {
				JOptionPane.showMessageDialog(this, strings.getText("ERROR_NO_EDGES"), 
						strings.getText("ERROR_LABEL"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			else if(algorithm == cPanelPointer.G_TOP_SORT) {
				algorithmActive = true;
				lockPanel();
				applyTopologicalSortAlgorithm();
			}
			else if(algorithm == cPanelPointer.MST_PRIM) {
				if(graph.isWeighted()) {
					if(!graph.isDirected()) {
						algorithmActive = true;
						lockPanel();
						applyPrimAlgorithm();
					}
					else // if graph is directed
						JOptionPane.showMessageDialog(this, strings.getText("ERROR_GRAPH_DIRECTED"), 
								strings.getText("ERROR_LABEL"), JOptionPane.ERROR_MESSAGE);
				}	
				else // if graph is not weighted
					JOptionPane.showMessageDialog(this, strings.getText("ERROR_GRAPH_NOT_WEIGHTED"), 
							strings.getText("ERROR_LABEL"), JOptionPane.ERROR_MESSAGE);
			}
			else if(algorithm == cPanelPointer.MST_KRUSKAL) {
				if(graph.isWeighted()) {
					if(!graph.isDirected()) {
						lockPanel();
						applyKruskalAlgorithm();
						algorithmActive = true;
					}
					else // if graph is directed
						JOptionPane.showMessageDialog(this, strings.getText("ERROR_GRAPH_DIRECTED"), 
								strings.getText("ERROR_LABEL"), JOptionPane.ERROR_MESSAGE);
				}	
				else // if graph is not weighted
					JOptionPane.showMessageDialog(this, strings.getText("ERROR_GRAPH_NOT_WEIGHTED"), 
							strings.getText("ERROR_LABEL"), JOptionPane.ERROR_MESSAGE);
			}
			else if(algorithm == cPanelPointer.SP_DIJKSTRA) {
				if(graph.isWeighted()) {
					if(!graph.hasNegativeEdges()) {
						lockPanel();
						applyDijkstraAlgorithm();
						algorithmActive = true;
					}
					else // if graph has negative edges
						JOptionPane.showMessageDialog(this, strings.getText("ERROR_NEGATIVE_EDGES"), 
								strings.getText("ERROR_LABEL"), JOptionPane.ERROR_MESSAGE);
				}	
				else // if graph is not weighted
					JOptionPane.showMessageDialog(this, strings.getText("ERROR_GRAPH_NOT_WEIGHTED"), 
							strings.getText("ERROR_LABEL"), JOptionPane.ERROR_MESSAGE);
			}
			else if(algorithm == cPanelPointer.SP_ALL_PAIRS) {
				if(graph.isWeighted()) {
					lockPanel();
					applyAllPairsAlgorithm();
					algorithmActive = true;
				}
				else // if graph is not weighted
					JOptionPane.showMessageDialog(this, strings.getText("ERROR_GRAPH_NOT_WEIGHTED"), 
							strings.getText("ERROR_LABEL"), JOptionPane.ERROR_MESSAGE);
			}
			else if(algorithm == cPanelPointer.SP_BELLMAN_FORD) {
				if(graph.isWeighted()) {
					lockPanel();
					applyBellManFordAlgorithm();
					algorithmActive = true;
				}
				else // if graph is not weighted
					JOptionPane.showMessageDialog(this, strings.getText("ERROR_GRAPH_NOT_WEIGHTED"), 
							strings.getText("ERROR_LABEL"), JOptionPane.ERROR_MESSAGE);
			}

			else if(algorithm == cPanelPointer.G_TRANSPOSE) {
				lockPanel();
				algorithmActive = true;
				if(graph.isDirected())
					printTransposeGraph(graph.transposeGraph());
				else
					printTransposeGraph(graph);
			}
		}
	}

	private void applyBreadthSearch() {
		runtime = Runtime.getRuntime();
		startTime = System.nanoTime();
		LinkedList<GraphNode> bfsSolution = graph.breadthFirstSearch();
		elapsedTime = System.nanoTime() - startTime;
		usedMemory = (runtime.totalMemory() - runtime.freeMemory());

		printTraverseSolution(bfsSolution);
	}

	private void applyDepthSearch() {
		runtime = Runtime.getRuntime();
		startTime = System.nanoTime();
		LinkedList<GraphNode> dfsSolution = graph.depthFirstSearch();
		elapsedTime = System.nanoTime() - startTime;
		usedMemory = (runtime.totalMemory() - runtime.freeMemory());

		printTraverseSolution(dfsSolution);
	}

	private void applyLimitedDepthSearch(int MAX_DEPTH) {
		runtime = Runtime.getRuntime();
		startTime = System.nanoTime();
		LinkedList<GraphNode> solution = graph.limitedDepthSearch(2);
		elapsedTime = System.nanoTime() - startTime;
		usedMemory = (runtime.totalMemory() - runtime.freeMemory());

		printTraverseSolution(solution);
	}

	private void applyPrimAlgorithm() {
		runtime = Runtime.getRuntime();
		startTime = System.nanoTime();
		LinkedList<GraphEdge> primSolution = new LinkedList<>();
		primSolution = graph.primAlgorithm();
		elapsedTime = System.nanoTime() - startTime;
		usedMemory = (runtime.totalMemory() - runtime.freeMemory());

		printMinimmunSpanningTree(primSolution);
	}

	private void applyKruskalAlgorithm() {
		runtime = Runtime.getRuntime();
		startTime = System.nanoTime();
		LinkedList<GraphEdge> kruskalSolution = new LinkedList<>();
		kruskalSolution = graph.primAlgorithm();
		elapsedTime = System.nanoTime() - startTime;
		usedMemory = (runtime.totalMemory() - runtime.freeMemory());

		printMinimmunSpanningTree(kruskalSolution);
	}

	private void applyDijkstraAlgorithm() {
		runtime = Runtime.getRuntime();
		startTime = System.nanoTime();
		dijkstraSolution = graph.Dijkstra().getFirst();
		elapsedTime = System.nanoTime() - startTime;
		usedMemory = (runtime.totalMemory() - runtime.freeMemory());

		clearCanvas();
		buffGraphics.setStroke(thickStroke);
		for(GraphNode cursor : graph.getNodes())
			cursor.reDraw(buffGraphics, cursor.getCenterX(), cursor.getCenterY());

		for(GraphEdge edge : graph.getEdges()) {
			buffGraphics.setStroke(stdStroke);
			buffGraphics.setColor(Color.black);
			edge.reDraw(buffGraphics,edge.getFromNode(), edge.getToNode(), graph.isWeighted(), graph.isDirected());
		}
		buffGraphics.setColor(Color.RED);
		buffGraphics.drawString(strings.getText("INFO_SELECT_NODE"), 10, getHeight() - 10);
		graphics.drawImage(offScreen,0 ,0, this);
	}

	private void applyBellManFordAlgorithm() {
		runtime = Runtime.getRuntime();
		startTime = System.nanoTime();
		if(startNode == null)
			bellmanFordSolution = graph.BellmanFord(1);
		else
			bellmanFordSolution = graph.BellmanFord(startNode.id());
		elapsedTime = System.nanoTime() - startTime;
		usedMemory = (runtime.totalMemory() - runtime.freeMemory());

		clearCanvas();
		buffGraphics.setStroke(thickStroke);
		for(GraphNode cursor : graph.getNodes())
			cursor.reDraw(buffGraphics, cursor.getCenterX(), cursor.getCenterY());

		for(GraphEdge edge : graph.getEdges()) {
			buffGraphics.setStroke(stdStroke);
			buffGraphics.setColor(Color.black);
			edge.reDraw(buffGraphics,edge.getFromNode(), edge.getToNode(), graph.isWeighted(), graph.isDirected());
		}
		buffGraphics.setColor(Color.RED);
		if(bellmanFordSolution != null)
			buffGraphics.drawString(strings.getText("INFO_SELECT_NODE"), 10, getHeight() - 10);
		else
			buffGraphics.drawString(strings.getText("ERROR_NEGATIVE_CYCLE"), 10, getHeight() - 10);
		graphics.drawImage(offScreen,0 ,0, this);
	}

	private void applyAllPairsAlgorithm() {
		clearCanvas();
		buffGraphics.setStroke(thickStroke);
		for(GraphNode cursor : graph.getNodes())
			cursor.reDraw(buffGraphics, cursor.getCenterX(), cursor.getCenterY());

		for(GraphEdge edge : graph.getEdges()) {
			buffGraphics.setStroke(stdStroke);
			buffGraphics.setColor(Color.black);
			edge.reDraw(buffGraphics,edge.getFromNode(), edge.getToNode(), graph.isWeighted(), graph.isDirected());
		}
		buffGraphics.setColor(Color.RED);
		buffGraphics.drawString(strings.getText("INFO_SELECT_NODES"), 10, getHeight() - 10);
		graphics.drawImage(offScreen,0 ,0, this);
	}

	private void applyIsConnectedAlgorithm() {
		runtime = Runtime.getRuntime();
		startTime = System.nanoTime();
		LinkedList<GraphNode> reachableNodes = graph.isConnected();
		elapsedTime = System.nanoTime() - startTime;
		usedMemory = (runtime.totalMemory() - runtime.freeMemory());

		printIsConnectedSoluion(reachableNodes);		
	}

	private void applyTopologicalSortAlgorithm() {
		runtime = Runtime.getRuntime();
		startTime = System.nanoTime();
		LinkedList<GraphNode> solution = graph.topologicalSort();
		elapsedTime = System.nanoTime() - startTime;
		usedMemory = (runtime.totalMemory() - runtime.freeMemory());

		if(solution != null)
			printTopologicalSorting(solution);
	}

	/**
	 * Print into the Graphics object
	 * the time of execution of an algorithm
	 */
	private void printExecutionTime() {
		finalTime = 0;//(60*60*1000F); // from Miliseconds to seconds
		//finalTime = (float) (Math.round(finalTime * 1000) / 1000.0); // rounding the number to 3 decimal points

		buffGraphics.setColor(Color.RED);
		buffGraphics.drawString(strings.getText("INFO_EXEC_TIME"), 10, getHeight() - 25);

		buffGraphics.setColor(Color.BLACK);
		String timeParsed = finalTime + " " + strings.getText("LABEL_SECONDS");
		if(strings.getCurrentLanguage() == strings.PT)
			buffGraphics.drawString(timeParsed, 145, getHeight() - 25);
		else if(strings.getCurrentLanguage() == strings.EN)
			buffGraphics.drawString(timeParsed, 110, getHeight() - 25);

	}

	private void printUsedMemory() {
		int mb = 1024 * 1024;
		usedMemory = usedMemory / mb;
		buffGraphics.setColor(Color.RED);
		buffGraphics.drawString(strings.getText("INFO_MEM_USED"), 10, getHeight() - 10);

		buffGraphics.setColor(Color.BLACK);
		String memParsed = usedMemory + " " + strings.getText("LABEL_MEGABYTES");
		if(strings.getCurrentLanguage() == strings.PT)
			buffGraphics.drawString(memParsed, 115, getHeight() - 10);
		else if(strings.getCurrentLanguage() == strings.EN)
			buffGraphics.drawString(memParsed, 110, getHeight() - 10);
	}
	/**
	 * Print solution computed by
	 * Breadth First Search and Depth
	 * First Search
	 * @param solution
	 */
	private void printTraverseSolution(LinkedList<GraphNode> solution) {
		clearCanvas();
		buffGraphics.setStroke(thickStroke);
		for(GraphNode cursor : graph.getNodes())
			cursor.reDraw(buffGraphics, cursor.getCenterX(), cursor.getCenterY());

		for(GraphEdge edge : graph.getEdges()) {
			buffGraphics.setStroke(stdStroke);
			buffGraphics.setColor(Color.black);
			edge.reDraw(buffGraphics,edge.getFromNode(), edge.getToNode(), graph.isWeighted(), graph.isDirected());
		}
		// print the algorithm solution
		buffGraphics.setColor(Color.RED);
		buffGraphics.drawString(strings.getText("INFO_SOLUTION"), 10, getHeight() - 40);
		buffGraphics.setColor(Color.BLACK);
		String solutionParsed = "";
		int solutionSize = solution.size();
		int count = 0;
		for(GraphNode node : solution) {
			count++;
			solutionParsed = solutionParsed + node.label().getText();
			if(count != solutionSize)
				solutionParsed = solutionParsed + " - ";
		}
		// print solution
		buffGraphics.drawString(solutionParsed, 70, getHeight() - 40);
		printExecutionTime();
		printUsedMemory();
		graphics.drawImage(offScreen,0 ,0, this);
	}

	private void printMinimmunSpanningTree(LinkedList<GraphEdge> solution) {
		int minWeight = 0;
		clearCanvas();
		buffGraphics.setStroke(thickStroke);
		for(GraphNode cursor : graph.getNodes())
			cursor.reDraw(buffGraphics, cursor.getCenterX(), cursor.getCenterY());

		for(GraphEdge edge : graph.getEdges()) {
			buffGraphics.setStroke(stdStroke);
			if(solution.contains(edge)) {
				minWeight = minWeight + edge.weight();
				edge.draw(buffGraphics, graph.isWeighted(), graph.isDirected(),Color.GREEN);
			}
			else
				edge.draw(buffGraphics, graph.isWeighted(), graph.isDirected(),Color.LIGHT_GRAY);
		}
		String solutionLabel = strings.getText("INFO_MIN_SPAN_WEIGHT");
		buffGraphics.setColor(Color.RED);
		buffGraphics.drawString(solutionLabel, 10, getHeight() - 40);
		buffGraphics.setColor(Color.BLACK);
		buffGraphics.drawString(minWeight + "", 55, getHeight() - 40);
		printExecutionTime();
		printUsedMemory();

		graphics.drawImage(offScreen,0 ,0, this);
	}

	private void printDijkstraSolution() {
		int minWeight = 0;
		// parse the solution
		LinkedList<GraphEdge> solution = new LinkedList<>();
		int index = finalNode.id();
		while(dijkstraSolution[index] != 0) {
			solution.add(graph.getEdge(dijkstraSolution[index], index));
			index = dijkstraSolution[index];
		}
		// print the graph
		clearCanvas();
		buffGraphics.setStroke(thickStroke);
		for(GraphNode cursor : graph.getNodes())
			cursor.reDraw(buffGraphics, cursor.getCenterX(), cursor.getCenterY());

		for(GraphEdge edge : graph.getEdges()) {
			buffGraphics.setStroke(stdStroke);
			if(solution.contains(edge)) {
				minWeight = minWeight + edge.weight();
				edge.draw(buffGraphics, graph.isWeighted(), graph.isDirected(),Color.GREEN);
			}
			else
				edge.draw(buffGraphics, graph.isWeighted(), graph.isDirected(),Color.LIGHT_GRAY);
		}
		String solutionParsed = strings.getText("INFO_MIN_WEIGHT") + " " + minWeight;
		buffGraphics.setColor(Color.RED);
		buffGraphics.drawString(solutionParsed, 10, getHeight() - 40);
		printExecutionTime();
		printUsedMemory();
		graphics.drawImage(offScreen,0 ,0, this);
	}

	private void printBellmanFordSolution() {
		int minWeight = Integer.MIN_VALUE;
		// parse the solution
		LinkedList<GraphEdge> solution = new LinkedList<>();
		int index = finalNode.id();
		while(bellmanFordSolution[index] != 0) {
			solution.add(graph.getEdge(bellmanFordSolution[index], index));
			index = bellmanFordSolution[index];
		}
		// print the graph
		clearCanvas();
		buffGraphics.setStroke(thickStroke);
		for(GraphNode cursor : graph.getNodes())
			cursor.reDraw(buffGraphics, cursor.getCenterX(), cursor.getCenterY());

		for(GraphEdge edge : graph.getEdges()) {
			buffGraphics.setStroke(stdStroke);
			if(solution.contains(edge)) { // print solution edges in a different colour
				if(minWeight == Integer.MIN_VALUE)
					minWeight = 0;
				minWeight = minWeight + edge.weight();
				edge.draw(buffGraphics, graph.isWeighted(), graph.isDirected(),Color.GREEN);
			}
			else
				edge.draw(buffGraphics, graph.isWeighted(), graph.isDirected(),Color.LIGHT_GRAY);
		}
		String solutionParsed;
		if(minWeight != Integer.MIN_VALUE)
			solutionParsed = strings.getText("INFO_MIN_WEIGHT") + " " + minWeight;
		else
			solutionParsed = strings.getText("INFO_MIN_WEIGHT") + " N/E";
		buffGraphics.setColor(Color.RED);
		buffGraphics.drawString(solutionParsed, 10, getHeight() - 40);
		printExecutionTime();
		printUsedMemory();
		graphics.drawImage(offScreen,0 ,0, this);
	}


	private void printFloydWarshallSolution() {
		runtime = Runtime.getRuntime();
		startTime = System.nanoTime();
		floydWarshallSolution = graph.FloydWarshall();
		elapsedTime = System.nanoTime() - startTime;
		usedMemory = (runtime.totalMemory() - runtime.freeMemory());

		for(int row = 1; row <= graph.size(); row++) {
			for(int line = 1; line <= graph.size(); line++) {
				System.out.print(floydWarshallSolution[row][line] + " ");
			}
			System.out.println();
		}

	}

	private void printIsConnectedSoluion(LinkedList<GraphNode> reachableNodes) {
		clearCanvas();
		buffGraphics.setStroke(thickStroke);
		for(GraphNode cursor : graph.getNodes())
			if(reachableNodes.contains(cursor))
				cursor.draw(buffGraphics, cursor.getCenterX(), cursor.getCenterY(), Color.GREEN);
			else
				cursor.reDraw(buffGraphics, cursor.getCenterX(), cursor.getCenterY());

		for(GraphEdge edge : graph.getEdges()) {
			buffGraphics.setStroke(stdStroke);
			buffGraphics.setColor(Color.black);
			edge.reDraw(buffGraphics,edge.getFromNode(), edge.getToNode(), graph.isWeighted(), graph.isDirected());
		}
		if((reachableNodes.size() == graph.size()) && (graph.isDirected())) // if graph is connected and directed
			buffGraphics.drawString(strings.getText("G_DIRECTED_CONNECTED"), 10, getHeight() - 40);

		else if((reachableNodes.size() == graph.size()) && (!graph.isDirected())) // if graph is connected and undirected
			buffGraphics.drawString(strings.getText("G_UNDIRECTED_CONNECTED"), 10, getHeight() - 40);

		else if(reachableNodes.size() < graph.size()) // if the graph isn't connected, directed or not
			buffGraphics.drawString(strings.getText("G_NOT_CONNECTED"), 10, getHeight() - 40);

		printExecutionTime();
		printUsedMemory();
		graphics.drawImage(offScreen,0 ,0, this);
	}

	private void printTopologicalSorting(LinkedList<GraphNode> solution) {
		clearCanvas();
		buffGraphics.setStroke(thickStroke);
		for(GraphNode cursor : graph.getNodes())
			cursor.reDraw(buffGraphics, cursor.getCenterX(), cursor.getCenterY());

		for(GraphEdge edge : graph.getEdges()) {
			buffGraphics.setStroke(stdStroke);
			buffGraphics.setColor(Color.black);
			edge.reDraw(buffGraphics,edge.getFromNode(), edge.getToNode(), graph.isWeighted(), graph.isDirected());
		}
		// print the algorithm solution
		buffGraphics.setColor(Color.RED);
		buffGraphics.drawString(strings.getText("INFO_SOLUTION"), 10, getHeight() - 40);
		buffGraphics.setColor(Color.BLACK);
		String solutionParsed = "";
		int solutionSize = solution.size();
		int count = 0;
		for(GraphNode node : solution) {
			count++;
			solutionParsed = solutionParsed + node.label().getText();
			if(count != solutionSize)
				solutionParsed = solutionParsed + " - ";
		}
		// print solution
		buffGraphics.drawString(solutionParsed, 70, getHeight() - 40);
		printExecutionTime();
		printUsedMemory();
		graphics.drawImage(offScreen,0 ,0, this);

	}

	private void printTransposeGraph(Graph transposeGraph) {
		clearCanvas();
		buffGraphics.setStroke(thickStroke);
		for(GraphNode cursor : transposeGraph.getNodes())
			cursor.reDraw(buffGraphics, cursor.getCenterX(), cursor.getCenterY());

		for(GraphEdge edge : transposeGraph.getEdges()) {
			buffGraphics.setStroke(stdStroke);
			buffGraphics.setColor(Color.black);
			edge.reDraw(buffGraphics,edge.getFromNode(), edge.getToNode(), graph.isWeighted(), graph.isDirected());
		}
		buffGraphics.setColor(Color.RED);
		buffGraphics.drawString(strings.getText("G_TRANSPOSE"), 10, getHeight() - 10);
		graphics.drawImage(offScreen,0 ,0, this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(graph != null) { // when the window is minimized or resized
			graphics = (Graphics2D) g;
			graphics.setRenderingHints(rh);
			buffGraphics.setRenderingHints(rh);
			buffGraphics.setStroke(stdStroke);
			graphics.drawImage(offScreen, 0, 0, this);
			//repaintGraph();
		}
		graphics = (Graphics2D) getGraphics();
		graphics.setRenderingHints(rh);
		buffGraphics.setRenderingHints(rh);
		buffGraphics.setStroke(stdStroke);
	}

	@Override
	public void update(Graphics g) {
		paintComponent(g);
	}

	private int parseUserInput(String userInput) {
		return Integer.parseInt(userInput);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		double touchX = e.getX();
		double touchY = e.getY();
		if(graph != null) {
			if(algorithmActive == false) { // when an algorithm is being applied
				// Add Node
				if(dToolsPointer.currentOperation() == dToolsPointer.ADD_NODE) {
					if(graph.isFull())
						JOptionPane.showMessageDialog(this, strings.getText("ERROR_GRAPH_FULL"), 
								strings.getText("ERROR_LABEL"), JOptionPane.WARNING_MESSAGE);
					else
						if(isSafeToDraw(touchX, touchY))
							drawNode(touchX, touchY);
				}

				// Remove Node
				else if(dToolsPointer.currentOperation() == dToolsPointer.RM_NODE) {
					if(!graph.getNodes().isEmpty())
						eraseAndRemoveNode(touchX, touchY);
				}
				else if((dToolsPointer.currentOperation() == dToolsPointer.ADD_EDGE
						|| dToolsPointer.currentOperation() == dToolsPointer.NONE_SELECTED) && graph.isWeighted()) { // change edge weight
					for(GraphEdge edge : graph.getEdges()) {
						if(edge.getLabelRectangle().contains(touchX, touchY)) {
							String userInput = "";
							userInput = JOptionPane.showInputDialog(null, strings.getText("EDGE_WEIGHT_INPUT"), 1);
							if(userInput != null) {
								if(userInput.matches("-?\\d+")) { // type numeric
									int weight = parseUserInput(userInput);
									graph.changeEdgeWeight(edge.getFromNode().id(), edge.getToNode().id(), weight);
									edge.setLabelText(edge.weight());
									repaintGraph();
								}
							}
						}
					}
				}
			}
			else { // if there's an algorithm being applied
				GraphNode node = getTouchedNode(touchX, touchY);
				if(node != null) {
					if(cPanelPointer.getSelectedAlgorithm() == cPanelPointer.SP_DIJKSTRA) {
						finalNode = node;
						printDijkstraSolution();
					}
					else if(cPanelPointer.getSelectedAlgorithm() == cPanelPointer.SP_BELLMAN_FORD && bellmanFordSolution != null) {
						finalNode = node;
						printBellmanFordSolution();
					}
					else if(cPanelPointer.getSelectedAlgorithm() == cPanelPointer.SP_ALL_PAIRS) {
						if(startNode == null) // when the user chooses the first node
							startNode = node;
						else if(startNode == node) // when the user deselects the first chosen node
							startNode = null;
						else if(finalNode == null) // when the user chooses the second node.
							finalNode = node;

						if(finalNode == null) { // when there is none, or one selected node
							clearCanvas();
							buffGraphics.setStroke(thickStroke);
							for(GraphNode cursor : graph.getNodes()) {
								if(startNode != null && cursor == startNode) // highlight the chosen node
									cursor.draw(buffGraphics, cursor.getCenterX(), cursor.getCenterY(), Color.ORANGE);
								else
									cursor.reDraw(buffGraphics, cursor.getCenterX(), cursor.getCenterY());
							}
							for(GraphEdge edge : graph.getEdges()) {
								buffGraphics.setStroke(stdStroke);
								buffGraphics.setColor(Color.black);
								edge.reDraw(buffGraphics,edge.getFromNode(), edge.getToNode(), graph.isWeighted(), graph.isDirected());
							}
							buffGraphics.setColor(Color.RED);
							if(startNode == null)
								buffGraphics.drawString(strings.getText("INFO_SELECT_NODES"), 10, getHeight() - 10);
							else
								buffGraphics.drawString(strings.getText("INFO_SELECT_SECOND_NODE"), 10, getHeight() - 10);
							graphics.drawImage(offScreen,0 ,0, this);
						}
						else { // when the user chooses the second node, the algorithm is applied
							applyBellManFordAlgorithm();
							printBellmanFordSolution();
						}
					}
				}
			}
		}
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		if(graph != null) {
			double touchX = e.getX();
			double touchY = e.getY();
			GraphNode node = getTouchedNode(touchX, touchY);
			if(algorithmActive == false) { // when the solution for an algorithm is not printed on the screen
				if(node != null) {
					// Dragging a Node
					if(dToolsPointer.currentOperation() != dToolsPointer.ADD_EDGE 
							&& dToolsPointer.currentOperation() != dToolsPointer.RM_EDGE) {
						if(isSafeToDrag(node, touchX, touchY)) {
							dragging = true;
							draggedNode = node;
							node.drag(buffGraphics, touchX, touchY);
							repaintGraph();
						}
					}
					// Add Edge
					else if(dToolsPointer.currentOperation() == dToolsPointer.ADD_EDGE) {
						if(fromNode == null)
							fromNode = node;
					}
					// remove Edge
					else if(dToolsPointer.currentOperation() == dToolsPointer.RM_EDGE) {
						if(fromNode == null)
							fromNode = node;
					}
				}
				/**
				 * If a user dragged a node too fast
				 * it wouldn't drag. This fixes that problem.
				 */
				if(node == null && dragging) {
					if(dToolsPointer.currentOperation() != dToolsPointer.ADD_EDGE 
							&& dToolsPointer.currentOperation() != dToolsPointer.RM_EDGE) {
						if(isSafeToDrag(draggedNode, touchX, touchY)) {
							draggedNode.drag(buffGraphics, touchX, touchY);
							repaintGraph();
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(graph != null ) {
			dragging = false;
			draggedNode = null;
			if(dToolsPointer.currentOperation() != dToolsPointer.ADD_NODE || 
					dToolsPointer.currentOperation() == dToolsPointer.RM_NODE) {
				double touchX = e.getX();
				double touchY = e.getY();
				GraphNode node = getTouchedNode(touchX, touchY);
				if(node != null && node != fromNode && fromNode != null) {
					if(dToolsPointer.currentOperation() == dToolsPointer.ADD_EDGE)
						drawEdge(node);
					else if(dToolsPointer.currentOperation() == dToolsPointer.RM_EDGE)
						eraseAndRemoveEdge(graph.getEdge(fromNode.id(), node.id()));
				}
				else { // if the user didn't drag to any node
					fromNode = null;
					node = null;
				}
			}
		}
	}

	// NOT USED
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
