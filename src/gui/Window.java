package gui;

import engine.Engine;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

public class Window extends JFrame implements MouseMotionListener, MouseListener {
	
	private static final long serialVersionUID = 1L; 
	
	private final int width = 1500;
	private final int height = 900;
	
	private final AlgorithmPanel algorithmPanel;
	private final DrawingPanel drawingPanel;
	
	public Window(Engine engine, DrawingPanel drawingPanel, MapGeneratorPanel mapGeneratorPanel, MapCreatorPanel mapCreatorPanel, SaveLoadPanel saveLoadPanel, AlgorithmPanel algorithmPanel) {
		super("Algorithm Visualiser");	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.fill = GridBagConstraints.VERTICAL;
	    c.gridx = 0;
	    c.gridy = 0;
	   
	    mapGeneratorPanel.setBorder(BorderFactory.createLineBorder(Color.black));
	    this.add(mapGeneratorPanel, c);
	    c.gridy = 1;
	    mapCreatorPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
	    this.add(mapCreatorPanel, c);
	    c.gridy = 2;
	    saveLoadPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
	    this.add(saveLoadPanel, c);
	    c.gridy = 3;
	    this.algorithmPanel = algorithmPanel;
	    algorithmPanel.setBorder(BorderFactory.createLineBorder(Color.black));
	    this.add(algorithmPanel,c);
	    
		
	    c.gridy = 0;
		c.gridx = 1;
		c.gridheight = 4;
		this.drawingPanel = drawingPanel;
		drawingPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		this.add(drawingPanel, c);
		drawingPanel.addMouseListener(this);
	}
	
	protected void editMode(boolean on) {
		if(on) {
			drawingPanel.addMouseMotionListener(this);
			drawingPanel.removeMouseListener(this);
		} else {
			drawingPanel.removeMouseMotionListener(this);
			drawingPanel.addMouseListener(this);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		drawingPanel.locationVisited(e.getX(), e.getY());
		this.repaint();
	}
	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		algorithmPanel.locationClick(e.getX(),e.getY());
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

}
