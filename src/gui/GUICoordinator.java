package gui;

import java.awt.Color;
import java.util.List;

import utility.AlgorithmStatistics;
import utility.Coordinate;
import data.AlgorithmType;
import engine.Engine;
import engine.graph.Node;
import engine.map.Map;

/*
 * Coordinates the GUI, is the entity to which the engine talks to
 * Is also the location of the main method
 */
public class GUICoordinator {
	
	private Engine engine;
	private Window window;
	private MapCreationPanel mapCreationPanel;
	private SaveLoadPanel saveLoadPanel;
	private AlgorithmPanel algorithmPanel;
	private DrawingPanel drawingPanel;
	
	private GUICoordinator() {
		super();
	}
	
	private void run() {
		engine = new Engine(this);
		drawingPanel = new DrawingPanel(800,800);
		mapCreationPanel = new MapCreationPanel(engine,this);
		saveLoadPanel = new SaveLoadPanel(engine, this);
		algorithmPanel = new AlgorithmPanel(engine);
		window = new Window(engine, drawingPanel, mapCreationPanel, saveLoadPanel, algorithmPanel);
		drawingPanel.setWindow(window);
		window.setVisible(true);
	}
	
	/*
	 * for dialog boxes
	 */
	public Window getWindow() {
		return window;
	}
	
	/*
	 * the next 4 methods are the interface presented to the engine 
	 */
	public void drawMap(Map map) {
		drawingPanel.drawMap(map);
	}
	
	public void drawPath(Map map, List<Coordinate> path, Color color) {
		drawingPanel.drawPath(map, path, color);
	}
	
	public void setAlgorithmStatistics(AlgorithmStatistics algorithmStatistics, AlgorithmType algorithmType) {
		algorithmPanel.setAlgorithmStatistics(algorithmStatistics, algorithmType);
	}
	
	public void resetAlgorithmPanel() {
		algorithmPanel.reset();
	}
	
	public static void main(String args[]) {
		GUICoordinator coordinator = new GUICoordinator();
		coordinator.run();
	}

}
