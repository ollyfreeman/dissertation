package gui;

import java.awt.Color;
import utility.AlgorithmStatistics;
import data.AlgorithmType;
import engine.Engine;
import engine.graph.Node;
import engine.map.Map;

public class GUICoordinator {
	
	private Engine engine;
	private Window window;
	private MapCreationPanel mapCreationPanel;
	private AlgorithmPanel algorithmPanel;
	private DrawingPanel drawingPanel;
	
	public GUICoordinator() {
		super();
	}
	
	private void run() {
		engine = new Engine(this);
		drawingPanel = new DrawingPanel(800,800);
		mapCreationPanel = new MapCreationPanel(engine,this);
		algorithmPanel = new AlgorithmPanel(engine);
		window = new Window(engine, drawingPanel, mapCreationPanel, algorithmPanel);
		drawingPanel.setWindow(window);
		window.setVisible(true);
	}
	
	public void drawMap(Map map, int resolution) {
		drawingPanel.drawMap(map, resolution);
	}
	
	public void drawPath(Map map, Node n, Color color) {
		drawingPanel.drawPath(map, n, color);
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
