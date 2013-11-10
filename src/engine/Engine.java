package engine;

import java.awt.Color;
import engine.graph.Node;
import engine.map.MapGenerator;
import utility.AlgorithmStatistics;
import utility.MapCreationParameters;
import data.AlgorithmType;
import data.DoesRouteExist;
import engine.map.Map;
import gui.GUICoordinator;

public class Engine {
	
	private MapInstance mapInstance;
	private GUICoordinator coordinator;
	
	public Engine(GUICoordinator coordinator) {
		this.coordinator = coordinator;
	}
	
	public void plotMap(String filename) {
		/*
		 * TODO save previous: saveMapInstance()
		 * 
		 * TODO load (deserialize) map instance: MapInstanceLoader.load(filename)
		 * should return filename
		 * 
		 * TODO plot map
		 * MapInstance.drawMap()
		 */
	}
	
	public void plotMap(MapCreationParameters mcp) {
		/*
		 * Parse the parameters
		 * if I develop multiple ways to make maps then I'll have to change this parsing stage
		 */
		int width = mcp.getWidth();
		int height = mcp.getHeight();
		int resolution = mcp.getResolution();
		int coveragePercentage = mcp.getCoverage();
		int clusteringScore = mcp.getClustering();
		
		/*
		 * create a map instance with the specific parameters
		 */
		Map map = MapGenerator.generateMap(width, height, coveragePercentage, clusteringScore);
		
		/*
		 * create a map instance with the specific map
		 */
		mapInstance = new MapInstance(map);
		
		/*
		 * plot map
		 */
		coordinator.drawMap(mapInstance.getMap(), resolution);
	}
	
	public void getAlgorithmStatistics(AlgorithmType algorithmType){
		AlgorithmStatistics algorithmStatistics = null;
		
		DoesRouteExist doesRouteExist = mapInstance.doesRouteExist();
		if(doesRouteExist.equals(DoesRouteExist.DontKnow)) {
			mapInstance.createAlgorithmData(AlgorithmType.AStar);
		}
		doesRouteExist = mapInstance.doesRouteExist();
		if (doesRouteExist.equals(DoesRouteExist.Yes)) {
			//actually do the algorithm if it hasn't already been done
			algorithmStatistics = mapInstance.createAlgorithmData(algorithmType);
			//return a statistics object with the statistics wrapped up
		} else {
			//do nothing, i.e. return null
		}
		coordinator.setAlgorithmStatistics(algorithmStatistics, algorithmType);
	}
	
	public void plotPath(AlgorithmType algorithmType, Color color) {
		/*
		 * TODO see if a path exists for that map instance
		 * TODO see if that algorithm has already been created for that map instance
		 * TODO if neither of the above are true, call the algorithm, and then set the statistics accordingly
		 * 
		 * TODO plot the path in the specified colour
		 */
		Node n = mapInstance.getGoalNode(algorithmType);
		coordinator.drawPath(mapInstance.getMap(), n, color);
	}
	
	private void loadMapInstance(String filename) {
		/*
		 * TODO deserialize the map instance with the given filename
		 * MapInstanceImportExport.load(filename)
		 */
	}
	
	private String saveMapInstance() {
		/*
		 * TODO serialize the map instance with a descriptive filename
		 * MapInstanceImportExport.save()
		 * TODO return filename
		 */
		return "";
	}



}
