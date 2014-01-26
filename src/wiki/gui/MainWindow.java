package wiki.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;


@SuppressWarnings("serial")
public class MainWindow extends JFrame implements ActionListener {
	private static final int LANG_PT = 0;
	private static final int LANG_EN = 1;
	public static final String title = "WikiGraph";
	public static final String version = "1.0 BETA version";
	private int currentLanguage;

	//private MainPanel mainPanel;
	private MainPanel mainPanel;
	private ControlPanel controlPanel; // pointer


	public static int currentAlg = ControlPanel.NONE_SELECTED;

	private final static TextStrings strings = new TextStrings();
	private final JMenuBar menuBar;
	private final JMenu fileMenu;		// holds load and save functionality
	private final JMenuItem loadMenu;
	private final JMenuItem saveMenu;
	private final JMenuItem aboutMenu;	// menu that holds info about the software and who made it
	private final JMenuItem howToMenu;	// Tutorials menu
	private final JMenu langMenu;		// choose the software language
	private final JMenu helpMenu;		// help/tutorial about the program

	private final JMenu othersMenu;		// other algorithms that can be applied to a graph
	private final JMenu miscMenu;
	private final JMenu connectedCompMenu;
	private final JMenu unSearchMenu;	// uninformed Search tab
	private final JMenu heuristicSearchMenu;
	private JRadioButtonMenuItem en;
	private JRadioButtonMenuItem pt;

	private static final JRadioButtonMenuItem topSortRadio = new JRadioButtonMenuItem();
	private static final JRadioButtonMenuItem isDagRadio = new JRadioButtonMenuItem();
	private static final JRadioButtonMenuItem isConnectedRadio = new JRadioButtonMenuItem();
	private static final JRadioButtonMenuItem reachVertexesRadio = new JRadioButtonMenuItem();
	private static final JRadioButtonMenuItem kosarajuRadio = new JRadioButtonMenuItem();
	private static final JRadioButtonMenuItem itDeepSearch = new JRadioButtonMenuItem();
	private static final JRadioButtonMenuItem uniformCostSearch = new JRadioButtonMenuItem();
	private final static JRadioButtonMenuItem transposeGraph = new JRadioButtonMenuItem();
	private final static JRadioButtonMenuItem limitedDepth = new JRadioButtonMenuItem();

	private static JRadioButtonMenuItem selectedRadioButton;

	public MainWindow() {
		selectedRadioButton = null;

		//mainPanel = new MainPanel(strings);
		//mainPanel.setPreferredSize(getSize());
		mainPanel = new MainPanel(strings);
		controlPanel = mainPanel.getControlPanel();
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		loadMenu = new JMenuItem();
		saveMenu = new JMenuItem();
		helpMenu = new JMenu();
		aboutMenu = new JMenuItem();
		howToMenu = new JMenuItem();

		langMenu = new JMenu();

		othersMenu = new JMenu();
		miscMenu = new JMenu();

		connectedCompMenu = new JMenu();
		heuristicSearchMenu = new JMenu();
		unSearchMenu = new JMenu();
		itDeepSearch.setEnabled(false);					// erase when implemented
		uniformCostSearch.setEnabled(false);			// erase when implemented

		currentLanguage = 0;
		setWindowProperties();
		setWindowSize();
		buildWindowElements(strings);
		strings.setComponentsText();
		algorithmSelection(false);
		setVisible(true);
		pack();
	}


	private void setWindowProperties() {
		setTitle(title + " " + "v" + version);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setWindowSize() {
		//setPreferredSize(new Dimension(778, 737));
		//setMaximumSize(new Dimension(778, Integer.MAX_VALUE));
		setMinimumSize(new Dimension(778, 737));
	}

	private void buildWindowElements(TextStrings strings) {		
		en = new JRadioButtonMenuItem();
		en.addActionListener(this);
		pt = new JRadioButtonMenuItem();
		pt.addActionListener(this);

		// used to set the text according to language
		strings.addMenu(fileMenu);
		fileMenu.setName("FILE_MENU");
		menuBar.add(fileMenu);

		// Others menu and his components
		strings.addMenu(othersMenu);
		strings.addMenu(othersMenu);
		strings.addMenu(miscMenu);
		strings.addRadioButtonMenu(topSortRadio);
		strings.addRadioButtonMenu(isDagRadio);
		strings.addRadioButtonMenu(isConnectedRadio);
		strings.addRadioButtonMenu(reachVertexesRadio);
		strings.addMenu(connectedCompMenu);
		strings.addRadioButtonMenu(kosarajuRadio);
		strings.addMenu(heuristicSearchMenu);
		strings.addMenu(unSearchMenu);
		strings.addRadioButtonMenu(itDeepSearch);
		strings.addRadioButtonMenu(uniformCostSearch);
		strings.addRadioButtonMenu(transposeGraph);
		strings.addRadioButtonMenu(limitedDepth);

		othersMenu.setName("OTHERS_MENU");
		topSortRadio.setName("G_TOP_SORT");
		isDagRadio.setName("G_IS_DAG");
		isConnectedRadio.setName("G_IS_CONNECTED");
		reachVertexesRadio.setName("G_REACH_VERT");
		miscMenu.setName("MISC_MENU");
		connectedCompMenu.setName("CON_COMP_MENU");
		kosarajuRadio.setName("G_KOSARAJU");
		heuristicSearchMenu.setName("HEURISTIC_SEARCH_MENU");
		unSearchMenu.setName("UN_SEARCH_MENU");
		itDeepSearch.setName("IT_DEEP_SEARCH");
		uniformCostSearch.setName("UNIFORM_COST_SEARCH");
		transposeGraph.setName("G_TRANSPOSE");
		limitedDepth.setName("T_LIMITED_DFS");

		topSortRadio.addActionListener(this);
		isConnectedRadio.addActionListener(this);
		isDagRadio.addActionListener(this);
		reachVertexesRadio.addActionListener(this);
		transposeGraph.addActionListener(this);
		limitedDepth.addActionListener(this);

		menuBar.add(othersMenu);
		othersMenu.add(miscMenu);
		othersMenu.add(connectedCompMenu);
		othersMenu.add(unSearchMenu);

		miscMenu.add(topSortRadio);
		miscMenu.add(isDagRadio);
		miscMenu.add(isConnectedRadio);
		miscMenu.add(reachVertexesRadio);
		miscMenu.add(transposeGraph);

		connectedCompMenu.add(kosarajuRadio);

		unSearchMenu.add(limitedDepth);
		unSearchMenu.add(itDeepSearch);
		othersMenu.add(heuristicSearchMenu);
		//othersMenu.add(uniformCostSearch);

		// language menu and his components
		strings.addMenu(langMenu);
		strings.addRadioButtonMenu(en);
		strings.addRadioButtonMenu(pt);
		langMenu.setName("LANG_MENU");
		en.setName("LANG_EN");
		pt.setName("LANG_PT");		
		pt.setSelected(true);
		langMenu.add(en);
		langMenu.add(pt);
		menuBar.add(langMenu);

		// help menu
		strings.addMenu(helpMenu);
		helpMenu.setName("HELP_MENU");
		menuBar.add(helpMenu);

		// about menu
		strings.addMenuItem(aboutMenu);
		aboutMenu.setName("ABOUT_MENU");
		aboutMenu.addActionListener(this);
		helpMenu.add(aboutMenu);

		// How to menu
		strings.addMenuItem(howToMenu);
		howToMenu.setName("HOW_TO_MENU");
		helpMenu.add(howToMenu);

		setJMenuBar(menuBar);
		getContentPane().add(mainPanel);
	}

	public static void algorithmSelection(boolean enable) {
		topSortRadio.setEnabled(enable);
		isDagRadio.setEnabled(false);
		isConnectedRadio.setEnabled(enable);
		reachVertexesRadio.setEnabled(false);
		kosarajuRadio.setEnabled(false);
		itDeepSearch.setEnabled(false);
		uniformCostSearch.setEnabled(false);
		transposeGraph.setEnabled(enable);
		limitedDepth.setEnabled(enable);
	}

	/**
	 * Returns if a radio button was selected.
	 * Only used in this class
	 * @param button
	 * @return
	 */
	private boolean selectRadioButton(JRadioButtonMenuItem button) {
		if(selectedRadioButton != null) { // if there's a button already selected
			selectedRadioButton.setSelected(false);
			if(selectedRadioButton != button) { // if the user clicked on different button
				selectedRadioButton = button;
				selectedRadioButton.setSelected(true);
				return true;
			}
			selectedRadioButton = null;
			controlPanel.setCurrentSelectedAlgorithm(ControlPanel.NONE_SELECTED);
			return false;
		}
		// if there is no selected button
		selectedRadioButton = button;
		selectedRadioButton.setSelected(true);

		return true;
	}

	public static void deselectAlgorithm() {
		if(selectedRadioButton != null) {
			selectedRadioButton.setSelected(false);
			selectedRadioButton = null;
			currentAlg = ControlPanel.NONE_SELECTED;
		}
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if(action.getSource() == en) {
			if(currentLanguage != LANG_EN) {
				pt.setSelected(false);
				strings.setLanguage(TextStrings.EN);
				currentLanguage = LANG_EN;
			}
			else // maintain the language selected
				en.setSelected(true);
		}
		else if(action.getSource() == pt) {
			if(currentLanguage != LANG_PT) {
				en.setSelected(false);
				strings.setLanguage(TextStrings.PT);
				currentLanguage = LANG_PT;
			}
			else // maintain the language selected
				pt.setSelected(true);
		}
		else if(action.getSource() == aboutMenu) {
			JOptionPane.showMessageDialog(this, strings.getText("COPYRIGHTS"), 
					strings.getText("ABOUT_LABEL"), JOptionPane.INFORMATION_MESSAGE);
		}
		else if(action.getSource() == topSortRadio) {
			if(selectRadioButton(topSortRadio)) {
				controlPanel.setCurrentSelectedAlgorithm(ControlPanel.G_TOP_SORT);
				currentAlg = ControlPanel.G_TOP_SORT;
			}
			else
				deselectAlgorithm();
		}

		else if(action.getSource() == isDagRadio) {
			if(selectRadioButton(isDagRadio)) {
				controlPanel.setCurrentSelectedAlgorithm(ControlPanel.G_IS_DAG);
				currentAlg = ControlPanel.G_IS_DAG;
			}
			else
				deselectAlgorithm();
		}

		else if(action.getSource() == isConnectedRadio) {
			if(selectRadioButton(isConnectedRadio)) {
				controlPanel.setCurrentSelectedAlgorithm(ControlPanel.G_IS_CONNECTED);
				currentAlg = ControlPanel.G_IS_CONNECTED;
			}
			else
				deselectAlgorithm();
		}

		else if(action.getSource() == reachVertexesRadio) {
			if(selectRadioButton(reachVertexesRadio)) {
				controlPanel.setCurrentSelectedAlgorithm(ControlPanel.G_REACH_VERT);
				currentAlg = ControlPanel.G_REACH_VERT;
			}
			else
				deselectAlgorithm();
		}

		else if(action.getSource() == kosarajuRadio) {
			if(selectRadioButton(kosarajuRadio)) {
				controlPanel.setCurrentSelectedAlgorithm(ControlPanel.G_KOSARAJU);
				currentAlg = ControlPanel.G_KOSARAJU;
			}
			else
				deselectAlgorithm();
		}
		else if(action.getSource() == transposeGraph) {
			if(selectRadioButton(transposeGraph)) {
				controlPanel.setCurrentSelectedAlgorithm(ControlPanel.G_TRANSPOSE);
				currentAlg = ControlPanel.G_TRANSPOSE;
			}
			else
				deselectAlgorithm();
		}
		else if(action.getSource() == limitedDepth) {
			if(selectRadioButton(limitedDepth)) {
				controlPanel.setCurrentSelectedAlgorithm(ControlPanel.T_LIMITED_DFS);
				currentAlg = ControlPanel.T_LIMITED_DFS;
			}
			else
				deselectAlgorithm();
		}
	}
}
