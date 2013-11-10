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


/*
 * This class is the coordinator for the back end. It is the interface to which
 * the front end (usually buttons in JPanels) sends messages.
 * 
 * Its main job is to create, load, save and delegate to the current mapInstance.
 */
public class Engine {
	
	private MapInstance mapInstance;
	private GUICoordinator coordinator;
	
	public Engine(GUICoordinator coordinator) {
		this.coordinator = coordinator;
	}
	
	/*
	 * loads a map with the given filename, and draws it to the GUI
	 */
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
	
	/*
	 * loads a map with the given parameters, and draws it to the GUI
	 */
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
	
	/*
	 * obtains the statistics for an algorithm.
	 * If no algorithm has been run it will run A* first to see if a route even exists
	 */
	public void getAlgorithmStatistics(AlgorithmType algorithmType){
		AlgorithmStatistics algorithmStatistics = null;
		
		/*
		 * find out if a route on this map exists: DontKnow means not yet calculated
		 */
		DoesRouteExist doesRouteExist = mapInstance.doesRouteExist();
		/*
		 * if a route has not yet been attempted on the map, we use (simple) A* to see
		 * if one even exists
		 */
		if(doesRouteExist.equals(DoesRouteExist.DontKnow)) {
			mapInstance.createAlgorithmData(AlgorithmType.AStar);
		}
		/*
		 * recheck doesRouteExist now that we've actually tried to find one.
		 */
		doesRouteExist = mapInstance.doesRouteExist();
		assert !(doesRouteExist.equals(doesRouteExist.DontKnow)) : "Just attempted to calculate route. It should not be \"DontKnow\""; 
		
		if (doesRouteExist.equals(DoesRouteExist.Yes)) {
			/*
			 * actually do the algorithm if it hasn't already been done
			 */
			algorithmStatistics = mapInstance.createAlgorithmData(algorithmType);
		} else {
			/*
			 * do nothing, i.e. algorithmStatistics = null tells the algorithmPanel that no path was found
			 */
		}
		/*
		 * return a statistics object with the statistics wrapped up
		 */
		coordinator.setAlgorithmStatistics(algorithmStatistics, algorithmType);
	}
	
	/*
	 * draws the path given the algorithm type and a colour
	 */
	public void plotPath(AlgorithmType algorithmType, Color color) {
		assert mapInstance.doesRouteExist().equals(DoesRouteExist.Yes) : "Should not be able to plot a route if either we don't know if a route exists or if a route doesn't exist";
		/*
		 * get the goal node for that algorithm
		 */
		Node n = mapInstance.getGoalNode(algorithmType);
		assert n != null : "Should not be able to plot a route if that route hasn't been calculated";
		/*
		 * plot the path in the specified colour
		 */
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
