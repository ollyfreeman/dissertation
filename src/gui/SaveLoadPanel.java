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
		loadInstanceButton = new JButton("LOAD!");
	    loadInstanceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(coordinator.getWindow());
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	            	engine.loadMapInstance(fc.getSelectedFile().getName());
	            }
			}
		});
	    this.loadInstanceButton.setEnabled(false);
		this.add(loadInstanceButton,c);
	}
	
	protected void enablePanel() {
		saveInstanceButton.setEnabled(true);
		loadInstanceButton.setEnabled(true);
	}
	
	protected void disablePanel() {
		saveInstanceButton.setEnabled(false);
		loadInstanceButton.setEnabled(false);
	}

}
