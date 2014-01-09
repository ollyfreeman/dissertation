package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import utility.AlgorithmStatistics;
import utility.Coordinate;
import data.AlgorithmType;
import engine.Engine;

/*
 * panel that displays the statistics for the algorithms on 
 * the current map
 */
public class AlgorithmPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Engine engine;
	private Component[][] componentArray = new Component[8][6];	
	private JTextField sourceCoordinateField;	private Coordinate sourceCoordinate;	private JRadioButton sourceButton;
	private JTextField goalCoordinateField;		private Coordinate goalCoordinate;		private JRadioButton goalButton;
	private boolean coordinatesEditable = false;
	private int mapWidth = 0;
	private int mapHeight = 0;

	public AlgorithmPanel(Engine engine) {

		this.engine = engine;

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 0;

		JLabel titleLabel = new JLabel("Algorithms");
		this.add(titleLabel,c);

		componentArray[0][0] = new JLabel("");
		componentArray[0][1] = new JLabel("Distance");
		componentArray[0][2] = new JLabel("Angle");
		componentArray[0][3] = new JLabel("Time");
		componentArray[0][4] = new JLabel("");
		componentArray[0][5] = new JLabel("");

		addAlgorithm("Dijkstra", AlgorithmType.Dijkstra, Color.GRAY, 1);
		addAlgorithm("A*", AlgorithmType.AStar, Color.ORANGE, 2);
		addAlgorithm("A* - smoothed", AlgorithmType.AStarSmoothed, Color.RED, 3);
		addAlgorithm("Basic Theta*", AlgorithmType.ThetaStar, Color.BLUE, 4);
		addAlgorithm("Lazy Theta*", AlgorithmType.LazyThetaStar, Color.CYAN, 5);
		addAlgorithm("Block A*", AlgorithmType.BlockAStar, Color.MAGENTA, 6);
		addAlgorithm("A* - visibility graph", AlgorithmType.AStarVisibility, Color.GREEN, 7);

		for(int i=0; i<componentArray.length; i++) {
			c.gridy=i;
			for(int j=0; j<componentArray[0].length; j++) {
				c.gridx=j;
				this.add(componentArray[i][j],c);
			}
		}
		c.gridx=0;
		c.gridy++;
		JLabel blankLabel = new JLabel(" ");
		this.add(blankLabel,c);
		c.gridy++;
		JLabel coordinateTitleLabel = new JLabel("Coordinates");
		this.add(coordinateTitleLabel,c);
		c.gridx++;
		JLabel startCoordinateLabel = new JLabel("Start");
		this.add(startCoordinateLabel,c);
		c.gridx++;
		JLabel goalCoordinateLabel = new JLabel("Goal");
		this.add(goalCoordinateLabel,c);
		c.gridy++;
		c.gridx=1;
		sourceCoordinateField = new JTextField(6);
		sourceCoordinateField.setEditable(false);
		this.add(sourceCoordinateField,c);
		c.gridx++;
		goalCoordinateField = new JTextField(6);
		goalCoordinateField.setEditable(false);
		this.add(goalCoordinateField,c);

		c.gridy++;
		c.gridx=1;
		sourceButton = new JRadioButton();
		sourceButton.setActionCommand("source");
		sourceButton.setSelected(true);
		sourceButton.setEnabled(false);
		this.add(sourceButton,c);
		goalButton = new JRadioButton();
		goalButton.setActionCommand("goal");
		goalButton.setSelected(false);
		goalButton.setEnabled(false);
		c.gridx++;
		this.add(goalButton,c);
		ButtonGroup sourceOrGoalButtonGroup = new ButtonGroup();
		sourceOrGoalButtonGroup.add(sourceButton);
		sourceOrGoalButtonGroup.add(goalButton);
	}

	private void addAlgorithm(String name, AlgorithmType algorithmType, Color color, int index) {
		componentArray[index][0] = new JLabel(name); 					componentArray[index][0].setForeground(color);
		JTextField textField;
		textField = new JTextField(6); 	textField.setEditable(false);	componentArray[index][1] = textField;
		textField = new JTextField(6); 	textField.setEditable(false);	componentArray[index][2] = textField;
		textField = new JTextField(6); 	textField.setEditable(false);	componentArray[index][3] = textField;

		final AlgorithmType algType = algorithmType;	//for inner classes
		final Color c = color;

		JButton calculateButton = new JButton("Calculate");
		calculateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				requestAlgorithmStatistics(algType);
			}
		});
		calculateButton.setEnabled(false);
		componentArray[index][4] = calculateButton;

		JButton drawButton = new JButton("Draw");
		drawButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawAlgorithm(algType, c);
			}
		});
		drawButton.setEnabled(false);
		componentArray[index][5] = drawButton;
	}

	/*
	 * called by calculate button
	 */
	private void requestAlgorithmStatistics(AlgorithmType algorithmType) {
		coordinatesEditable = false;	sourceButton.setEnabled(false);	goalButton.setEnabled(false);
		engine.createAlgorithmStatistics(algorithmType, sourceCoordinate, goalCoordinate);
	}

	/*
	 * called by draw button
	 */
	private void drawAlgorithm(AlgorithmType algorithmType, Color color) {
		engine.plotPath(algorithmType, color);
	}

	/*
	 * called by Coordinator to write statistics to this panel
	 */
	protected void setAlgorithmStatistics(AlgorithmStatistics algorithmStatistics, AlgorithmType algorithmType) {
		if(algorithmStatistics == null) {
			setNoPath();
		} else {
			int index;
			if(algorithmType.equals(AlgorithmType.Dijkstra)) {
				index = 1;
			} else if (algorithmType.equals(AlgorithmType.AStar)){
				index = 2;
			} else if (algorithmType.equals(AlgorithmType.AStarSmoothed)){
				index = 3;
			} else if (algorithmType.equals(AlgorithmType.ThetaStar)){
				index = 4;
			} else if (algorithmType.equals(AlgorithmType.LazyThetaStar)){
				index = 5;
			} else if (algorithmType.equals(AlgorithmType.BlockAStar)) {
				index = 6;
			} else {
				index = 7;
			}
			DecimalFormat f = new DecimalFormat("##.00");
			JTextField distanceLabel = (JTextField) componentArray[index][1];
			distanceLabel.setText(f.format(algorithmStatistics.getDistance()));
			JTextField angleLabel = (JTextField) componentArray[index][2];
			angleLabel.setText(f.format(algorithmStatistics.getAngle()));	
			JTextField timeLabel = (JTextField) componentArray[index][3];
			timeLabel.setText(f.format(algorithmStatistics.getTime()));	
			JButton calculateButton = (JButton) componentArray[index][4];
			calculateButton.setEnabled(false);
			JButton drawButton = (JButton) componentArray[index][5];
			drawButton.setEnabled(true);
			//add source and goal coord, and nodes expanded and load time
		}
	}

	/*
	 * called by Coordinator to clear all fields when we load/generate a new map
	 */
	protected void resetPanel(int width, int height) {
		for(int i=1; i<componentArray.length; i++) {
			JTextField distanceLabel = (JTextField) componentArray[i][1];
			distanceLabel.setText("");
			JTextField angleLabel = (JTextField) componentArray[i][2];
			angleLabel.setText("");
			JTextField timeLabel = (JTextField) componentArray[i][3];
			timeLabel.setText("");
			JButton calculateButton = (JButton) componentArray[i][4];
			calculateButton.setEnabled(false);
			JButton drawButton = (JButton) componentArray[i][5];
			drawButton.setEnabled(false);

			this.mapWidth=width;
			this.mapHeight=height;
			sourceCoordinateField.setText("(0,0)");
			goalCoordinateField.setText("("+width+","+height+")");
			sourceCoordinate = new Coordinate(0,0);
			goalCoordinate = new Coordinate(width,height);
			sourceButton.setEnabled(true);
			goalButton.setEnabled(true);
			sourceButton.setSelected(true);
			coordinatesEditable = true;
		}
	}

	/*
	 * called by Coordinator to enable all calculation buttons
	 */
	protected void enablePanel() {
		for(int i=1; i<componentArray.length; i++) {
			JButton calculateButton = (JButton) componentArray[i][4];
			calculateButton.setEnabled(true);
		}
	}

	/*
	 * use by setAlgorithmStatistics() if there is no path
	 */
	private void setNoPath() {
		for(int i=1; i<componentArray.length; i++) {
			JTextField distanceLabel = (JTextField) componentArray[i][1];
			distanceLabel.setText("no path");
			JTextField angleLabel = (JTextField) componentArray[i][2];
			angleLabel.setText("no path");
			JTextField timeLabel = (JTextField) componentArray[i][3];
			timeLabel.setText("no path");
			JButton calculateButton = (JButton) componentArray[i][4];
			calculateButton.setEnabled(false);
			JButton drawButton = (JButton) componentArray[i][5];
			drawButton.setEnabled(false);
		}
	}

	protected void locationClick(int x, int y) {
		if(this.mapWidth!=0 && this.mapHeight!=0) {
			if(coordinatesEditable) {
				int w = 800;
				int h = 800;
				/* had this in when I had some maps with half width
				if(this.mapWidth < this.mapHeight) {
					w = 400;
				}
				if(x<=w) {
				*/
					Coordinate c = new Coordinate((x*mapWidth)/w,(y*mapHeight)/h);
					if(sourceButton.isSelected()) {
						sourceCoordinate = c;
						sourceCoordinateField.setText("("+c.getX()+","+c.getY()+")");
					} else {
						goalCoordinate = c;
						goalCoordinateField.setText("("+c.getX()+","+c.getY()+")");
					}
				//}
			}
		}
	}

}
