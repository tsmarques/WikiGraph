package wiki.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class DrawingTools extends JPanel implements ActionListener {
	public final int NONE_SELECTED = -1;
	public final int ADD_NODE = 0;
	public final int RM_NODE = 1;
	public final int ADD_EDGE = 2;
	public final int RM_EDGE = 3;
	private final int BUTTON_HEIGHT = 53;
	private final int BUTTON_WIDTH = 53;

	private Dimension dimension;
	private final Toolkit tkt;
	private final JToggleButton buttons[];
	//private final GridLayout layout;
	private int currentOp;

	DrawingTools(TextStrings strings) {
		buttons = new JToggleButton[4];
		//layout = new GridLayout(4, 0);
		tkt = Toolkit.getDefaultToolkit();
		currentOp = NONE_SELECTED;

		setPanelProperties();
		buildButtons(strings);
		//strings.setComponentsText();
	}

	public int currentOperation() {
		return currentOp;
	}

	public void disableButtons() {
		if(currentOp != NONE_SELECTED)
			buttons[currentOp].setSelected(false);
		for(int i = 0; i < 4; i++)
			buttons[i].setEnabled(false);
	}

	public void enableButtons() {
		for(int i = 0; i < 4; i++)
			buttons[i].setEnabled(true);
		buttons[ADD_NODE].setSelected(true);
		setCurrentOperation(ADD_NODE);
	}

	private void buildButtons(TextStrings strings) {
		int width = (int)(0.04 * (dimension.width));

		for(int i = 0; i < 4; i++) {
			buttons[i] = new JToggleButton();
			buttons[i].setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			buttons[i].addActionListener(this);
			strings.addToggleButton(buttons[i]); // used to set the text according to the chosen language
			buttons[i].setEnabled(false);
			add(buttons[i]);
		}

		buttons[ADD_NODE].setName("BT_ADD_NODE");
		buttons[ADD_NODE].setText("AN");
		//buttons[ADD_NODE].setIcon(new ImageIcon(this.getClass().getResource("addNodeButton.png")));
		//System.out.println(this.getClass().getResource("addNodeButton.png") == null);

		buttons[RM_NODE].setName("BT_RM_NODE");
		buttons[RM_NODE].setText("RN");

		buttons[ADD_EDGE].setName("BT_ADD_EDGE");
		buttons[ADD_EDGE].setText("AE");

		buttons[RM_EDGE].setName("BT_RM_EDGE");
		buttons[RM_EDGE].setText("RE");
	}

	private void setPanelProperties() {
		dimension = tkt.getScreenSize();

		// Panel size
		/*int width = (int)(0.04 * (dimension.width));
		int height = (int)(0.50 * (dimension.height));

		setSize(new Dimension(width,height));*/
		// Panel coordinates
		/*int x = (int)(0.50 * (dimension.width));
		int y = (int)(0.49 * (dimension.height));

		setLocation(x, y);*/
		setVisible(true);
		setBackground(Color.LIGHT_GRAY);
	}

	private void setCurrentOperation(int currentOp) {
		if(this.currentOp != NONE_SELECTED)
			buttons[this.currentOp].setSelected(false); // unselect previous button in use
		this.currentOp = currentOp;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if(currentOp != -1) { // if a button is already pressed
			if(action.getSource() == buttons[currentOp]) { // if the presssed button is pressed again
				buttons[currentOp].setSelected(false);
				currentOp = NONE_SELECTED;
				return;
			}
		}

		if(action.getSource() == buttons[ADD_NODE])
			setCurrentOperation(ADD_NODE);
		else if(action.getSource() == buttons[RM_NODE])
			setCurrentOperation(RM_NODE);

		else if(action.getSource() == buttons[RM_NODE])
			setCurrentOperation(RM_NODE);

		else if(action.getSource() == buttons[ADD_EDGE])
			setCurrentOperation(ADD_EDGE);

		else if(action.getSource() == buttons[RM_EDGE])
			setCurrentOperation(RM_EDGE);
	}
}
