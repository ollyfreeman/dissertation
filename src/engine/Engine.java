package engine;

import java.awt.Color;
import java.io.*;
import java.util.List;

import engine.map.MapGenerator;
import utility.AlgorithmStatistics;
import utility.Coordinate;
import utility.MapCreationParameters;
import data.AlgorithmType;
import data.DoesRouteExist;
import engine.map.Map;
import gui.GUICoordinator;


/*
 * This class is the coordinator for the back end. It is the interface to which
 * the front end (usually buttons in JPanels) sends messages.
 * 
 * Its main job is to create, load, save and delegate to the current MapInstance.
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
	public void plotMapFromParameters(MapCreationParameters mcp) {
		/*
		 * Parse the parameters
		 * if I develop multiple ways to make maps then I'll have to change this parsing stage
		 */
		int width = mcp.getWidth();
		int height = mcp.getHeight();
		//int resolution = mcp.getResolution();			not needed
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
		coordinator.drawMap(mapInstance.getMap());
		
		/*
		 * plotting a new map so need to reset the Algorithm panel
		 */
		coordinator.resetAlgorithmPanel(mapInstance.getMap().getWidth(),mapInstance.getMap().getHeight());
		coordinator.enableAlgorithmPanel();
	}
	
	/*
	 * loads a map with the given int 2D array, and draws it to the GUI
	 */
	public void plotMapFrom2DArray(int[][] array) {
		
		/*
		 * create a map instance with the specific parameters
		 */
		Map map = new Map(array);
		
		/*
		 * create a map instance with the specific map
		 */
		mapInstance = new MapInstance(map);
		
		/*
		 * plot map
		 */
		coordinator.drawMap(mapInstance.getMap());
	}
	
	
	/*
	 * obtains the statistics for an algorithm.
	 * If no algorithm has been run it will run A* first to see if a route even exists
	 */
	public void createAlgorithmStatistics(AlgorithmType algorithmType, Coordinate source, Coordinate goal){
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
			mapInstance.createAlgorithmData(AlgorithmType.Dijkstra, source, goal,false);		//OR Dijkstra? if you change this need to change MapInstance.doesRouteExist()
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
			algorithmStatistics = mapInstance.createAlgorithmData(algorithmType, source, goal,false);
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
		List<Coordinate> path = mapInstance.getPath(algorithmType);
		assert path != null : "Should not be able to plot a route if that route hasn't been calculated";
		/*
		 * plot the path in the specified colour
		 */
		coordinator.drawPath(path, color);
	}
	
	/*
	 * draws the expanded nodes given the algorithm type and a colour
	 */
	public void plotNodes(AlgorithmType algorithmType, Color color) {
		assert mapInstance.doesRouteExist().equals(DoesRouteExist.Yes) : "Should not be able to plot a route if either we don't know if a route exists or if a route doesn't exist";
		/*
		 * get the goal node for that algorithm
		 */
		List<Coordinate> path = mapInstance.getPath(algorithmType);
		int[][] nea = mapInstance.getNodesExpandedArray(algorithmType);
		assert path != null : "Should not be able to plot a route if that route hasn't been calculated";
		/*
		 * plot the path in the specified colour
		 */
		coordinator.drawNodes(nea, color);
	}
	
	public void loadMapInstance(String filename) {
		 try {
			 FileInputStream fileIn = new FileInputStream(filename);
			 ObjectInputStream in = new ObjectInputStream(fileIn);
			 mapInstance = (MapInstance) in.readObject();
			 in.close();
			 fileIn.close();
			 coordinator.drawMap(mapInstance.getMap());
			 coordinator.resetAlgorithmPanel(mapInstance.getMap().getWidth(),mapInstance.getMap().getHeight());
			 coordinator.enableAlgorithmPanel();
			 int unfoundPaths = 0;
			 for(AlgorithmType algorithmType: AlgorithmType.values()) {
				 if (this.mapInstance.getPath(algorithmType) != null) {
					 coordinator.setAlgorithmStatistics(mapInstance.createAlgorithmData(algorithmType, mapInstance.getAlgorithmData(algorithmType).getSource(),mapInstance.getAlgorithmData(algorithmType).getGoal(),false), algorithmType);
					 unfoundPaths++;
				 }
			 }
			 if (unfoundPaths == 5) {
				 coordinator.setAlgorithmStatistics(null, AlgorithmType.Dijkstra);
				 coordinator.resetAlgorithmPanel(mapInstance.getMap().getWidth(),mapInstance.getMap().getHeight());
				 coordinator.enableAlgorithmPanel();
			 }
			 
		 } catch(IOException i) {
			 i.printStackTrace();
			 return;
		 } catch (ClassNotFoundException c) {
			 c.printStackTrace();
			 return;
		 }
	}
	

	public void saveMapInstance(String filename) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(mapInstance);
			outStream.close();
			fileOut.close();
		} catch(IOException i) {
			i.printStackTrace();
		}
	}
	
	public void saveMapOnly(String filename) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(new MapInstance(mapInstance.getMap()));
			outStream.close();
			fileOut.close();
		} catch(IOException i) {
			i.printStackTrace();
		}
	}



}
