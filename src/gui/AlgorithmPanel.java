package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utility.AlgorithmStatistics;
import data.AlgorithmType;
import engine.Engine;

/*
 * panel that displays the statistics for the algorithms on 
 * the current map
 */
public class AlgorithmPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Engine engine;
	private Component[][] componentArray = new Component[4][6];

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
		
		addAlgorithm("A*", AlgorithmType.AStar, Color.ORANGE, 1);
		addAlgorithm("A*Smoothed", AlgorithmType.AStarSmoothed, Color.RED, 2);
		addAlgorithm("Theta*", AlgorithmType.ThetaStar, Color.BLUE, 3);
	    
	    for(int i=0; i<componentArray.length; i++) {
    		c.gridy = i;
	    	for(int j=0; j<componentArray[0].length; j++) {
	    		c.gridx = j;
	    		this.add(componentArray[i][j],c);
	    	}
	    }
		
	}
	
	private void addAlgorithm(String name, AlgorithmType algorithmType, Color color, int index) {
	    componentArray[index][0] = new JLabel(name);
	    componentArray[index][1] = new JTextField(6);
	    componentArray[index][2] = new JTextField(6);
	    componentArray[index][3] = new JTextField(6);
	    
	    final AlgorithmType algType = algorithmType;	//for inner classes
	    final Color c = color;
	    
	    JButton calculateButton = new JButton("Calculate");
	    calculateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				requestAlgorithmStatistics(algType);
			}
		});
	    componentArray[index][4] = calculateButton;
	   
	    JButton aStarDraw = new JButton("Draw");
	    aStarDraw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawAlgorithm(algType, c);
			}
		});
	    aStarDraw.setEnabled(false);
	    componentArray[index][5] = aStarDraw;
	}
	
	/*
	 * called by calculate button
	 */
	private void requestAlgorithmStatistics(AlgorithmType algorithmType) {
		engine.getAlgorithmStatistics(algorithmType);
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
			if(algorithmType.equals(AlgorithmType.AStar)) {
				index = 1;
			} else if (algorithmType.equals(AlgorithmType.AStarSmoothed)){
				index = 2;
			} else { //Theta*
				index = 3;
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
		}
		
	}
	
	/*
	 * called by Coordinator to clear all fields when we load/generate a new map
	 */
	protected void reset() {
		for(int i=1; i<componentArray.length; i++) {
			JTextField distanceLabel = (JTextField) componentArray[i][1];
			distanceLabel.setText("");
			JTextField angleLabel = (JTextField) componentArray[i][2];
			angleLabel.setText("");
			JTextField timeLabel = (JTextField) componentArray[i][3];
			timeLabel.setText("");
			JButton calculateButton = (JButton) componentArray[i][4];
			calculateButton.setEnabled(true);
			JButton drawButton = (JButton) componentArray[i][5];
			drawButton.setEnabled(false);
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

}
