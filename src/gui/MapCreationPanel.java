package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utility.MapCreationParameters;
import engine.Engine;

/*
 * Panel for creating Potential Maps
 */
public class MapCreationPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Engine engine;
	private GUICoordinator coordinator;
	private JComboBox<String> resolution;
	private JTextField coverageField;
	private JTextField clusteringField;

	public MapCreationPanel(Engine engine, GUICoordinator coordinator) {
		
		this.engine = engine;
		this.coordinator = coordinator;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.fill = GridBagConstraints.VERTICAL;
	    c.gridx = 0;
	    c.gridy = 0;
	    
	    JLabel titleLabel = new JLabel("Map Generator");
		this.add(titleLabel,c);
		
		c.gridx = 0;
	    c.gridy = 1;
		JLabel resolutionLabel = new JLabel("Resolution");
		this.add(resolutionLabel,c);
		c.gridx = 1;
		resolution = new JComboBox<String>(data.MapResolutions.getResolutionStringArray());
		this.add(resolution,c);
		
		c.gridx = 0;
	    c.gridy = 2;
		JLabel coverageLabel = new JLabel("Coverage");
		this.add(coverageLabel,c);
		c.gridx = 1;
		coverageField = new JTextField(5);
		coverageField.setText("30");
		this.add(coverageField,c);
		
		c.gridx = 0;
	    c.gridy = 3;
		JLabel clusteringLabel = new JLabel("Clustering");
		this.add(clusteringLabel,c);
		c.gridx = 1;
		clusteringField = new JTextField(5);
		clusteringField.setText("50");
		this.add(clusteringField,c);
		
		c.gridx = 0;
	    c.gridy = 4;
	    c.gridwidth = 2;
		JButton createMapButton = new JButton("GENERATE!");
		createMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				plotMap();
			}
		});
		this.add(createMapButton,c);
		
	}
	
	private void plotMap() {
		MapCreationParameters mcp = getMapCreationParameters();
		coordinator.resetAlgorithmPanel();						//plotting a new map so need to reset the Algorithm panel
		engine.plotMap(mcp);
	}
	
	/*
	 * pack up parameters into a MapCreationParameters object
	 */
	private MapCreationParameters getMapCreationParameters() {
		int index = resolution.getSelectedIndex();
		int resolution = 1;
		for(int i=0; i<(index/2); i++) {		
			resolution*=2;
		}
		String[] resolutionArray = data.MapResolutions.getResolutionStringArray();
		String resolutionString = resolutionArray[index];
		String[] stringArray = resolutionString.split("x");
		MapCreationParameters mcp = new MapCreationParameters(Integer.parseInt(stringArray[0]), Integer.parseInt(stringArray[1]), resolution, Integer.parseInt(coverageField.getText()), Integer.parseInt(clusteringField.getText()));
		return mcp;
	}

}
