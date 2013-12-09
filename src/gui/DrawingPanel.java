package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import utility.Coordinate;
import engine.map.Map;

/*
 * Panel that maps and paths are drawn onto
 * calling draw map or draw path alter the state of this object, so that
 * when paint is called (via a call to 'repaint') the behaviour is appropriate
 */
public class DrawingPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	private final int width;
	private final int height;
	
	//these are for displaying a map and routes
	private Map map;
	private int resolution;
	private List<List<Coordinate>> paths;
	private List<Color> pathColours;
	
	//these are for creating a new map
	private boolean creationMode;
	private int[][] canvasArray;
	private boolean isBrush;
	private int brushSize;
	
	private Window window;					//need this because calling repaint on just drawingPanel (this) seems glitchy, so call it on window

	public DrawingPanel(int width, int height) {
		this.width = width;
		this.height = height;
		this.paths = new LinkedList<List<Coordinate>>();
		this.pathColours = new LinkedList<Color>();
		this.creationMode = false;
	}
	
	public void setWindow(Window window) {
		this.window = window;				//need this because calling repaint on just this panel seems glitchy
	}

	public Dimension getPreferredSize() {
		return new Dimension(width,height);
	}

	public void drawMap(Map map) {
		this.resolution = data.MapResolutions.getResolutionFromMapHeight(map.getHeight());
		this.map = map;
		this.paths = new LinkedList<List<Coordinate>>();		//i.e. reset this for the new map
		pathColours = new LinkedList<Color>();	//i.e. reset this for the new map
		window.repaint();					//need this because calling repaint on just this panel seems glitchy
	}

	public void drawPath(Map map, List<Coordinate> path, Color color) {
		if(!pathColours.contains(color)) {
			paths.add(path);
			pathColours.add(color);
		}
		window.repaint();
	}
	
	//for creation mode
	protected void startEditMode(int x, int y) {
		this.map = null;
		this.paths = new LinkedList<List<Coordinate>>();
		this.pathColours = new LinkedList<Color>();
		this.creationMode = true;
		this.isBrush = true;
		this.resolution = width/y;
		this.canvasArray = new int[x][y];
		for(int j=0; j<y; j++) {
			for(int i=0;i<x; i++) {
				canvasArray[i][j]=1;
			}
		}
		this.window.repaint();
	}
	
	//for creation mode
	protected void toBrush(boolean b) {
		this.isBrush = b;
	}
	//for creation mode
	protected int[][] stopEditMode() {
		this.creationMode = false;
		return canvasArray;
	}
	//for creation mode
	protected void locationVisited(int x, int y) {
		try {
			canvasArray[x/resolution][y/resolution] = this.isBrush ? 0 : 1;
		} catch (ArrayIndexOutOfBoundsException e) {
			//do nothing - if we try to set a location outside the panel then just ignore
		}
		for(int j = -((brushSize/resolution)/2); j < ((brushSize/resolution)/2); j++) {
			for(int i = -((brushSize/resolution)/2); i < ((brushSize/resolution)/2); i++) {
				try {
					canvasArray[(x/resolution)+i][(y/resolution)+j] = this.isBrush ? 0 : 1;
				} catch (ArrayIndexOutOfBoundsException e) {
					//do nothing - if we try to set a location outside the panel then just ignore
				}
			}
		}
		this.window.repaint();
	}
	
	protected void setBrushSize(int brushSize) {
		this.brushSize = brushSize;
	}
	

	public void paintComponent(Graphics g) { 
		if(creationMode) {
			g.setColor(Color.BLACK);
			for(int y=0;y < canvasArray[0].length; y++) {
				for(int x=0; x < canvasArray.length; x++) {
					if(canvasArray[x][y] == 0) {
						g.fillRect(x*resolution, y*resolution, resolution, resolution);
					}
				}
			}
			g.fillRect((canvasArray.length*resolution), 0, width-(canvasArray.length*resolution), height);	//fill the rest of the panel as black
		} else {
			if(map != null) {
				g.setColor(Color.BLACK);
				for(int y=0;y < map.getHeight(); y++) {
					for(int x=0; x < map.getWidth(); x++) {
						if(map.getCell(x,y).isBlocked()) {
							g.fillRect(x*resolution, y*resolution, resolution, resolution);
						}
					}
				}
				g.fillRect(map.getWidth()*resolution, 0, width-map.getWidth()*resolution, height);	//fill the rest of the panel as black
				for(int i = 0; i<paths.size(); i++) {
					List<Coordinate> path = paths.get(i);
					if(path==null) {
						//TODO should never enter get here, as a goal node shouldn't be added if it's null (i.e. no path possible/yet calculated
						System.out.println("No path, exiting...");
					} else {
						g.setColor(pathColours.get(i));
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
	
}

