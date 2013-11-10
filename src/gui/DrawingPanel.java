package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import utility.Coordinate;
import engine.graph.Node;
import engine.map.Map;


public class DrawingPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Map map;
	private int width;
	private int height;
	private int resolution;
	private List<Node> goalNodes;
	private List<Color> pathColours;
	
	private Window window;					//need this because calling repaint on just this panel seems glitchy

	public DrawingPanel(int width, int height) {
		this.width = width;
		this.height = height;
		this.goalNodes = new LinkedList<Node>();
		this.pathColours = new LinkedList<Color>();
	}
	
	public void setWindow(Window window) {
		this.window = window;				//need this because calling repaint on just this panel seems glitchy
	}

	public Dimension getPreferredSize() {
		return new Dimension(width,height);
	}

	public void drawMap(Map map, int resolution) {
		this.resolution = resolution;
		this.map = map;
		goalNodes = new LinkedList<Node>();	//i.e. reset this for the new map
		pathColours = new LinkedList<Color>();
		window.repaint();					//need this because calling repaint on just this panel seems glitchy
	}

	public void drawPath(Map map, Node n, Color color) {
		if(!goalNodes.contains(n)) {
			goalNodes.add(n);
			pathColours.add(color);
		}
		window.repaint();
	}

	public void paintComponent(Graphics g) {      
		if(map != null) {
			g.setColor(Color.BLACK);
			for(int y=0;y < map.getHeight(); y++) {
				for(int x=0; x < map.getWidth(); x++) {
					if(map.getCell(x,y).isBlocked()) {
						g.setColor(Color.BLACK);
						g.fillRect(x*resolution, y*resolution, resolution, resolution);
					}
				}
			}
			for(int i = 0; i<goalNodes.size(); i++) {
				Node node = goalNodes.get(i);
				List<Coordinate> path = new LinkedList<Coordinate>();
				if(node==null) {
					//TODO should never enter get here, as a goal node shouldn't be added if it's null (i.e. no path possible/yet calculated
					System.out.println("No path, exiting...");
				} else {
					g.setColor(pathColours.get(i));
					while(node != null) {
						Coordinate coordinate = new Coordinate(node.getX(), node.getY());
						path.add(0, coordinate);
						node = node.getParent();
					}
					int j = 0;
					while(j+1 < path.size()) {
						Coordinate from = path.get(j);
						Coordinate to = path.get(j+1);
						g.drawLine((from.getX()*resolution)+(resolution/2), (from.getY()*resolution)+(resolution/2), (to.getX()*resolution)+(resolution/2), (to.getY()*resolution)+(resolution/2));
						j++;
					}
				}
			}
		}
	} 
	
}

