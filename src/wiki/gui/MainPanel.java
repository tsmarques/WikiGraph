package wiki.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class MainPanel extends JPanel {  
	private final Canvas dArea;
	private final ControlPanel controlPanel;
	private final DrawingTools dTools;
	//private final SpringLayout layout;
	private final JLabel errorLabel;
	
	
	private static final Toolkit tkt = Toolkit.getDefaultToolkit();
	Dimension dimension;
	
	public MainPanel(TextStrings strings) {
		dTools = new DrawingTools(strings);
		dArea = new Canvas(strings);
		controlPanel = new ControlPanel(strings, dArea, dTools);
		dArea.setPanelPointers(controlPanel, dTools);
		errorLabel = new JLabel();
		dimension = tkt.getScreenSize();
		setPanelProperties();
		buildPanelElements();
		//strings.setComponentsText();
	}
	
	private void setPanelProperties() {
		setLayout(new BorderLayout());
		setBackground(new Color(61, 61, 61));
	}
	
	private void buildPanelElements() {
		// Drawing Area
		
		//int height = (int)(dimension.getHeight() * 0.518);
		//int width = (int)(0.5305 * (dimension.getWidth()));
		
		dArea.setPreferredSize(new Dimension(724,397));
		
		// Control Panel
		int controlPanelWidth = (int)(0.1 * (dimension.getWidth())); // this value were assigned by experiment
		//int controlPanelHeight = (int)(0.516 * (dimension.getHeight()));
		int controlPanelHeight = 259;
		controlPanel.setPreferredSize(new Dimension(controlPanelWidth, (int)(controlPanelHeight / 1.53)));
		
		// Drawing tools panel
		int dToolsWidth = (int)(0.043 * (dimension.getWidth()));
		int dToolsHeight = 259;
		//int dToolsHeight = (int)(controlPanelHeight * 0.655);
		dTools.setPreferredSize(new Dimension(dToolsWidth, dToolsHeight));
		
		// Error Label
		errorLabel.setName("ERROR_LABEL");
		
		add(dArea, BorderLayout.SOUTH);
		add(controlPanel);
		add(dTools, BorderLayout.EAST);
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}
}
