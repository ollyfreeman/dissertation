package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import engine.Engine;

/*
 * Panel for creating Potential Maps
 */
public class SaveLoadPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final Engine engine;
	private final GUICoordinator coordinator;
	
	private JButton saveInstanceButton;
	private JButton saveMapButton;
	private JButton loadInstanceButton;

	public SaveLoadPanel(Engine engine, GUICoordinator coordinator) {
		
		this.engine = engine;
		this.coordinator = coordinator;
		
		initialise();
	}
	
	private void initialise() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.fill = GridBagConstraints.VERTICAL;
	    c.gridx = 0;
	    c.gridy = 0;
	    
	    JLabel titleLabel = new JLabel("Save/Load");
		this.add(titleLabel,c);
		
		c.gridx = 0;
	    c.gridy = 1;
	    saveInstanceButton = new JButton("SAVE!");
	    saveInstanceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long time = System.currentTimeMillis();
				JFileChooser fc = new JFileChooser();
				fc.setSelectedFile(new File("/Users/olly_freeman/Dropbox/Part2Project/serialized/" + time + ".ser"));
				int returnVal = fc.showSaveDialog(coordinator.getWindow());
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	            	engine.saveMapInstance(fc.getSelectedFile().getName());
	            }
			}
		});
	    this.saveInstanceButton.setEnabled(false);
		this.add(saveInstanceButton,c);
		
		c.gridy = 2;
	    saveMapButton = new JButton("SAVEM!");
	    saveMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long time = System.currentTimeMillis();
				JFileChooser fc = new JFileChooser();
				fc.setSelectedFile(new File("/Users/olly_freeman/Dropbox/Part2Project/serialized/" + time + "MAP.ser"));
				int returnVal = fc.showSaveDialog(coordinator.getWindow());
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	            	engine.saveMapOnly(fc.getSelectedFile().getName());
	            }
			}
		});
	    this.saveMapButton.setEnabled(false);
		this.add(saveMapButton,c);
		
		c.gridy = 3;
		loadInstanceButton = new JButton("LOAD!");
	    loadInstanceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(coordinator.getWindow());
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	            	engine.loadMapInstance(fc.getSelectedFile().getName());
	            	enablePanel();
	            }     
			}
		});
	    this.loadInstanceButton.setEnabled(true);
		this.add(loadInstanceButton,c);
	}
	
	protected void enablePanel() {
		saveInstanceButton.setEnabled(true);
		saveMapButton.setEnabled(true);			//DELETE
		loadInstanceButton.setEnabled(true);
	}
	
	protected void disablePanel() {
		saveInstanceButton.setEnabled(false);
		saveMapButton.setEnabled(false);			//DELETE
		loadInstanceButton.setEnabled(false);
	}

}
