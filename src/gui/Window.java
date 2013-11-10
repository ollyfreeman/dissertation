package gui;

import engine.Engine;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

public class Window extends JFrame {
	
	private static final long serialVersionUID = 1L; 
	
	private final int width = 1500;
	private final int height = 900;
	
	public Window(Engine engine, DrawingPanel drawingPanel, MapCreationPanel mapCreationPanel, AlgorithmPanel algorithmPanel) {
		super("Algorithm Visualiser");	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.fill = GridBagConstraints.VERTICAL;
	    c.gridx = 0;
	    c.gridy = 0;
	   
	    mapCreationPanel.setBorder(BorderFactory.createLineBorder(Color.black));
	    this.add(mapCreationPanel, c);
	    c.gridy = 1;
	    algorithmPanel.setBorder(BorderFactory.createLineBorder(Color.black));
	    this.add(algorithmPanel,c);
	    
		
	    c.gridy = 0;
		c.gridx = 1;
		c.gridheight = 2;
		drawingPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		this.add(drawingPanel, c);
	}

}
