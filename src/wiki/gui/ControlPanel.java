package wiki.gui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.SpringLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements ActionListener {
	public static final int NONE_SELECTED = -1;
	public static final int T_DFS = 0;
	public static final int T_BFS = 1;
	public static final int MST_PRIM = 2;
	public static final int MST_KRUSKAL = 3;
	public static final int SP_DIJKSTRA = 4;
	public static final int SP_BELLMAN_FORD = 5;
	//public static final int SP_FLOYD_WARSHALL = 6;
	public static final int SP_ALL_PAIRS = 6;
	public static final int G_KOSARAJU = 7;
	public static final int G_IS_CONNECTED = 8;
	public static final int G_IS_DAG = 9;
	public static final int G_REACH_VERT = 10;
	public static final int G_TOP_SORT = 11;
	public static final int G_TRANSPOSE = 13;
	public static final int T_LIMITED_DFS = 14;

	private int currentSelectedAlg;
	private final TextStrings strings;
	private final SpringLayout layout;
	private final JSeparator vSeparator;
	private final JSeparator horizontalSep;
	private final JSeparator horizontalSep2;

	private final Canvas canvasPointer;
	private final DrawingTools dToolsPointer;

	private final JButton newGraphButton;
	private final JButton changeButton;
	private final JButton resetButton;
	private final JButton applyButton;

	private final JLabel newGraphLabel;
	private final JLabel isWeightedLabel;
	private final JLabel isDirectedLabel;
	private final JLabel traversesLabel;
	private final JLabel dfsLabel;
	private final JLabel bfsLabel;
	private final JLabel mstLabel;
	private final JLabel mstKruskalLabel;
	private final JLabel mstPrimLabel;
	private final JLabel shortestPathLabel;
	private final JLabel dijkstraLabel;
	private final JLabel floydWarshallLabel;
	private final JLabel allPairslLabel;
	private final JLabel bellmanFordLabel;	
	private final JLabel othersLabel;

	private final JRadioButton isDirectedRadio;
	private final JRadioButton isWeightedRadio;
	private final JRadioButton dfsRadio;
	private final JRadioButton bfsRadio;
	private final JRadioButton mstPrim;
	private final JRadioButton mstKruskal;
	private final JRadioButton spDijkstra;
	//private final JRadioButton spFloydWarshall;
	private final JRadioButton spBellManFord;
	private final JRadioButton spAllPairs;
	private final JRadioButton[] algorithms;

	public ControlPanel(TextStrings strings, Canvas canvasPointer, DrawingTools dToolsPointer) {
		this.canvasPointer = canvasPointer;
		this.dToolsPointer = dToolsPointer;

		this.strings = strings;
		currentSelectedAlg = NONE_SELECTED;
		layout = new SpringLayout();
		vSeparator = new JSeparator(JSeparator.VERTICAL);
		horizontalSep = new JSeparator(JSeparator.HORIZONTAL);
		horizontalSep2 = new JSeparator(JSeparator.HORIZONTAL);

		newGraphButton = new JButton();
		changeButton = new JButton();
		resetButton = new JButton();
		applyButton = new JButton();

		isWeightedRadio = new JRadioButton();
		isDirectedRadio = new JRadioButton();

		newGraphLabel = new JLabel();
		dfsLabel = new JLabel();
		bfsLabel = new JLabel();

		isWeightedLabel = new JLabel();
		isDirectedLabel = new JLabel();
		traversesLabel = new JLabel();
		mstLabel = new JLabel();
		mstKruskalLabel = new JLabel();
		mstPrimLabel = new JLabel();
		shortestPathLabel = new JLabel();
		dijkstraLabel = new JLabel();
		bellmanFordLabel = new JLabel();
		floydWarshallLabel = new JLabel();
		allPairslLabel = new JLabel();
		othersLabel = new JLabel();

		dfsRadio = new JRadioButton();
		bfsRadio = new JRadioButton();
		mstPrim = new JRadioButton();
		mstKruskal = new JRadioButton();
		spDijkstra = new JRadioButton();
		spBellManFord = new JRadioButton();
		spAllPairs = new JRadioButton();
		algorithms = new JRadioButton[]{ dfsRadio, bfsRadio, mstPrim, mstKruskal, 
				spDijkstra, spBellManFord, /*spFloydWarshall*/ spAllPairs};

		buildPanelElements();
		setPanelProperties();
		positionPanelElements();
		disableAlgorithmSelection();
	}

	/**
	 * Returns the currently selected
	 * Algorithm.
	 * @return
	 */
	public int getSelectedAlgorithm() {
		return currentSelectedAlg;
	}

	public void setCurrentSelectedAlgorithm(int algorithm) {
		if(algorithm == NONE_SELECTED) {
			if(currentSelectedAlg < algorithms.length)
				algorithms[currentSelectedAlg].setSelected(false);
			currentSelectedAlg = NONE_SELECTED;
			applyButton.setEnabled(false);
		}
		else { // if there's a button selected
			if(currentSelectedAlg != algorithm) { // if the current button is not being clicked again
				if(currentSelectedAlg != NONE_SELECTED && currentSelectedAlg < algorithms.length)
					algorithms[currentSelectedAlg].setSelected(false);
				currentSelectedAlg = algorithm;
				applyButton.setEnabled(true);
			}
			else {
				currentSelectedAlg = NONE_SELECTED;
				applyButton.setEnabled(false);
			}
		}
	}

	private void setPanelProperties() {
		setBackground(Color.LIGHT_GRAY);
		setLayout(layout);
	}

	private void buildPanelElements() {
		// Separators
		vSeparator.setPreferredSize(new Dimension(2, 240));
		add(vSeparator);

		horizontalSep.setPreferredSize(new Dimension(465, 2));
		horizontalSep2.setPreferredSize(new Dimension(465, 2));
		add(horizontalSep);
		add(horizontalSep2);

		buildButtons();
		buildLabels();

	}

	private void buildButtons() {
		// Buttons
		newGraphButton.setName("BT_NEW_GRAPH");
		strings.addButton(newGraphButton);
		newGraphButton.setSize(new Dimension(90, 30));
		newGraphButton.addActionListener(this);
		add(newGraphButton);

		resetButton.setName("BT_RESET");
		strings.addButton(resetButton);
		resetButton.setPreferredSize(new Dimension(75, 25));
		resetButton.setEnabled(false); // starts disabled
		resetButton.addActionListener(this);
		add(resetButton);

		changeButton.setName("BT_CHANGE");
		strings.addButton(changeButton);
		changeButton.setPreferredSize(new Dimension(79, 25));
		changeButton.setEnabled(false); // start disabled
		changeButton.addActionListener(this);
		add(changeButton);

		applyButton.setName("BT_APPLY");
		strings.addButton(applyButton);
		applyButton.setEnabled(false); // start disabled
		applyButton.addActionListener(this);
		applyButton.setPreferredSize(new Dimension(85, 25));
		add(applyButton);

		for(JRadioButton radioButton : algorithms)
			radioButton.addActionListener(this);

		// Radio Buttons
		add(isDirectedRadio);
		add(isWeightedRadio);
		add(dfsRadio);
		add(bfsRadio);
		add(mstKruskal);
		add(mstPrim);
		add(spDijkstra);
		//add(spFloydWarshall);
		add(spAllPairs);
		add(spBellManFord);
	}

	private void buildLabels() {
		// Labels
		traversesLabel.setName("LABEL_TRAVERSES");
		strings.addLabel(traversesLabel);
		traversesLabel.setForeground(Color.BLACK);
		add(traversesLabel);

		dfsLabel.setName("LABEL_T_DFS");
		dfsLabel.setForeground(new Color(138, 79, 79));
		strings.addLabel(dfsLabel);
		add(dfsLabel);

		bfsLabel.setName("LABEL_T_BFS");
		bfsLabel.setForeground(new Color(138, 79, 79));
		strings.addLabel(bfsLabel);
		add(bfsLabel);

		newGraphLabel.setName("LABEL_NEW_GRAPH");
		strings.addLabel(newGraphLabel);
		newGraphLabel.setForeground(Color.BLACK);
		add(newGraphLabel);

		isWeightedLabel.setName("LABEL_IS_WEIGHTED");
		strings.addLabel(isWeightedLabel);
		add(isWeightedLabel);

		isDirectedLabel.setName("LABEL_IS_DIRECTED");
		strings.addLabel(isDirectedLabel);
		add(isDirectedLabel);

		shortestPathLabel.setName("LABEL_SHORT_PATH");
		strings.addLabel(shortestPathLabel);

		mstLabel.setName("LABEL_MST");
		strings.addLabel(mstLabel);
		mstLabel.setForeground(Color.BLACK);
		add(mstLabel);

		mstKruskalLabel.setName("MST_KRUSKAL");
		strings.addLabel(mstKruskalLabel);
		mstKruskalLabel.setForeground(new Color(138, 79, 79));
		add(mstKruskalLabel);

		mstPrimLabel.setName("MST_PRIM");
		strings.addLabel(mstPrimLabel);
		mstPrimLabel.setForeground(new Color(138, 79, 79));
		add(mstPrimLabel);

		shortestPathLabel.setName("LABEL_SHORT_PATH");
		strings.addLabel(shortestPathLabel);
		shortestPathLabel.setForeground(Color.BLACK);
		add(shortestPathLabel);

		dijkstraLabel.setName("SP_DIJKSTRA");
		strings.addLabel(dijkstraLabel);
		dijkstraLabel.setForeground(new Color(138, 79, 79));
		add(dijkstraLabel);

		floydWarshallLabel.setName("SP_FLOYD_WARSHALL");
		strings.addLabel(floydWarshallLabel);
		floydWarshallLabel.setForeground(new Color(138, 79, 79));
		//add(floydWarshallLabel);
		
		allPairslLabel.setName("SP_ALL_PAIRS");
		strings.addLabel(allPairslLabel);
		allPairslLabel.setForeground(new Color(138, 79, 79));
		add(allPairslLabel);

		bellmanFordLabel.setName("SP_BELLMAN_FORD");
		strings.addLabel(bellmanFordLabel);
		bellmanFordLabel.setForeground(new Color(138, 79, 79));
		add(bellmanFordLabel);

		othersLabel.setName("LABEL_OTHERS");
		strings.addLabel(othersLabel);
	}

	private void positionPanelElements() {
		// newGraphLabel
		layout.putConstraint(SpringLayout.WEST, newGraphLabel, 50, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, newGraphLabel, 5, SpringLayout.NORTH, this);

		// Is Directed Label
		layout.putConstraint(SpringLayout.WEST, isDirectedLabel, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, isDirectedLabel, 50, SpringLayout.NORTH, this);

		// Is Weighted label
		layout.putConstraint(SpringLayout.WEST, isWeightedLabel, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, isWeightedLabel, 80, SpringLayout.NORTH, this);

		// Buttons
		layout.putConstraint(SpringLayout.WEST, newGraphButton, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, newGraphButton, 130, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, resetButton, 45, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, resetButton, 205, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, changeButton, 90, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, changeButton, 130, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, applyButton, 40, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, applyButton, 170, SpringLayout.NORTH, this);

		// Radio Buttons
		layout.putConstraint(SpringLayout.WEST, isDirectedRadio, 130, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, isDirectedRadio, 50, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, isWeightedRadio, 130, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, isWeightedRadio, 80, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, dfsRadio, 270 + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, dfsRadio, 45, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, bfsRadio, 555  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, bfsRadio, 45, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, mstKruskal, 270  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, mstKruskal, 120, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, mstPrim, 555  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, mstPrim, 120, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, spDijkstra, 270  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, spDijkstra, 200, SpringLayout.NORTH, this);

		/*layout.putConstraint(SpringLayout.WEST, spFloydWarshall, 412, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, spFloydWarshall, 200, SpringLayout.NORTH, this);*/
		
		layout.putConstraint(SpringLayout.WEST, spAllPairs, 412  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, spAllPairs, 200, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, spBellManFord, 555  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, spBellManFord, 200, SpringLayout.NORTH, this);

		// Separators
		layout.putConstraint(SpringLayout.WEST, vSeparator, 190, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, vSeparator, 10, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, horizontalSep, 199  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, horizontalSep, 75, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, horizontalSep2, 199  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, horizontalSep2, 150, SpringLayout.NORTH, this);

		// Labels		
		layout.putConstraint(SpringLayout.WEST, traversesLabel, 365  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, traversesLabel, 5, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, dfsLabel, 220 + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, dfsLabel, 25, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, bfsLabel, 515  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, bfsLabel, 25, SpringLayout.NORTH, this);

		// MST LABELS
		layout.putConstraint(SpringLayout.WEST, mstLabel, 340  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, mstLabel, 85, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, mstKruskalLabel, 255  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, mstKruskalLabel, 102, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, mstPrimLabel, 550  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, mstPrimLabel, 102, SpringLayout.NORTH, this);

		// Shortest Path Label
		layout.putConstraint(SpringLayout.WEST, shortestPathLabel, 375  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, shortestPathLabel, 158, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, dijkstraLabel, 250  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, dijkstraLabel, 180, SpringLayout.NORTH, this);

		/*layout.putConstraint(SpringLayout.WEST, floydWarshallLabel, 375, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, floydWarshallLabel, 180, SpringLayout.NORTH, this);*/
		
		layout.putConstraint(SpringLayout.WEST, allPairslLabel, 370  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, allPairslLabel, 180, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, bellmanFordLabel, 520  + 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, bellmanFordLabel, 180, SpringLayout.NORTH, this);
	}

	/**
	 * Allows the user to select an
	 * algorithm
	 */
	public void enableAlgorithmSelection() {
		for(int i = 0; i < algorithms.length; i++)
			algorithms[i].setEnabled(true);
	}

	public void disableAlgorithmSelection() {
		applyButton.setEnabled(false);
		for(int i = 0; i < algorithms.length; i++)
			algorithms[i].setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == newGraphButton) {
			boolean isDirected = isDirectedRadio.isSelected();
			boolean isWeighted = isWeightedRadio.isSelected();
			canvasPointer.newGraph(isDirected, isWeighted);
			dToolsPointer.enableButtons();

			newGraphButton.setEnabled(false);
			resetButton.setEnabled(true);
			changeButton.setEnabled(true);
			isDirectedRadio.setEnabled(false);
			if(isDirectedRadio.isSelected())
				isDirectedLabel.setEnabled(true);
			else
				isDirectedLabel.setEnabled(false);

			MainWindow.algorithmSelection(true);	// algorithms that are in the menu 
			enableAlgorithmSelection();
		}
		else if(event.getSource() == resetButton) {
			if(canvasPointer.reset()) { // retrieves if it was a full reset
				dToolsPointer.disableButtons();

				newGraphButton.setEnabled(true);
				resetButton.setEnabled(false);
				changeButton.setEnabled(false);

				isDirectedRadio.setSelected(false);
				isWeightedRadio.setSelected(false);
				isDirectedRadio.setEnabled(true);
				isDirectedLabel.setEnabled(true);
				if(currentSelectedAlg != NONE_SELECTED && currentSelectedAlg < algorithms.length)
					algorithms[currentSelectedAlg].setSelected(false);
				else
					MainWindow.deselectAlgorithm();

				MainWindow.algorithmSelection(false);
				disableAlgorithmSelection();
			}
			else { // step back
				if(currentSelectedAlg < algorithms.length)
					algorithms[currentSelectedAlg].setSelected(false);
				else
					MainWindow.deselectAlgorithm();
				setCurrentSelectedAlgorithm(NONE_SELECTED);
			}
		}
		else if(event.getSource() == changeButton)
			canvasPointer.setGraphWeighted(isWeightedRadio.isSelected());

		else if(event.getSource() == applyButton)
			canvasPointer.applyAlgorithm(currentSelectedAlg);

		else {
			for(int i = 0; i < algorithms.length; i++)
				if(event.getSource() == algorithms[i])
					setCurrentSelectedAlgorithm(i);
		}
	}
}
