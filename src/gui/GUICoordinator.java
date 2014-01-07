package gui;

import java.awt.Color;
import java.util.List;

import utility.AlgorithmStatistics;
import utility.Coordinate;
import data.AlgorithmType;
import engine.Engine;
import engine.map.Map;

/*
 * Coordinates the GUI, is the entity to which the engine talks to
 * Is also the location of the main method
 */
public class GUICoordinator {
	
	private Engine engine;
	private Window window;
	private MapGeneratorPanel mapGeneratorPanel;
	private MapCreatorPanel mapCreatorPanel;
	private SaveLoadPanel saveLoadPanel;
	private AlgorithmPanel algorithmPanel;
	private DrawingPanel drawingPanel;
	
	private GUICoordinator() {
		super();
	}
	
	private void run() {
		engine = new Engine(this);
		drawingPanel = new DrawingPanel(800,800,this);
		mapGeneratorPanel = new MapGeneratorPanel(engine,this);
		mapCreatorPanel = new MapCreatorPanel(engine,this);
		saveLoadPanel = new SaveLoadPanel(engine, this);
		algorithmPanel = new AlgorithmPanel(engine);
		window = new Window(engine, drawingPanel, mapGeneratorPanel, mapCreatorPanel, saveLoadPanel, algorithmPanel);
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
	 * for map creation
	 */
	protected DrawingPanel getDrawingPanel(){
		return drawingPanel;
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
	
	//where x and y are the width and height in cells (not pixels)
	public void startEditMode(int x, int y) {
		drawingPanel.startEditMode(x,y);
		window.editMode(true);
		mapGeneratorPanel.disablePanel();
		saveLoadPanel.disablePanel();
		resetAlgorithmPanel(x,y);
	}
	
	public int[][] stopEditMode() {
		window.editMode(false);
		mapGeneratorPanel.enablePanel();
		saveLoadPanel.enablePanel();
		enableAlgorithmPanel();
		return drawingPanel.stopEditMode();
	}
	
	public void resetAlgorithmPanel(int x, int y) {
		algorithmPanel.resetPanel(x,y);
	}
	
	public void enableAlgorithmPanel() {
		algorithmPanel.enablePanel();
	}
	
	//needed only by MapGenerationPanel to enable this after first map is generated
	public void enableSaveLoadPanel() {
		saveLoadPanel.enablePanel();
	}
	
	public void setBrushSize(int brushSize) {
		drawingPanel.setBrushSize(brushSize);
	}
	
	public static void main(String args[]) {
		GUICoordinator coordinator = new GUICoordinator();
		coordinator.run();
	}

}
