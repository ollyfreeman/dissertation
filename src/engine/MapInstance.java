package engine;

import utility.AlgorithmStatistics;
import data.AlgorithmType;
import data.DoesRouteExist;
import engine.graph.Graph;
import engine.graph.Node;
import engine.map.Map;
import engine.graph.GraphGenerator;

/*
 * this class represents all of the data about a particular map
 * it contains the map itself, data about 
 */
public class MapInstance {
	
	private final Map map;
	private Graph graph; //other graphs are cloned from this - BUT not implemented graph cloning yet
	
	private DoesRouteExist doesRouteExist;
	
	private AlgorithmData aStarData;
	private AlgorithmData aStarSmoothedData;
	private AlgorithmData thetaStarData;
	
	public MapInstance(Map map) {
		this.map = map;
		this.doesRouteExist = DoesRouteExist.DontKnow;
	}
	
	protected Map getMap() {
		return map;
	}
	
	protected void setGraph() {
		graph = GraphGenerator.generateGraph(map);
	}
	
	/*
	 * use AStar to determine whether or not a route exists
	 */
	protected DoesRouteExist doesRouteExist() {
		if(!(this.doesRouteExist.equals(DoesRouteExist.No) || this.doesRouteExist.equals(DoesRouteExist.Yes))) {
			if(aStarData == null) {
				this.doesRouteExist = DoesRouteExist.DontKnow;
			} else if (aStarData.getGoalNode() == null) {
				this.doesRouteExist = DoesRouteExist.No;
			} else {
				this.doesRouteExist = DoesRouteExist.Yes;
			}
		}
		return this.doesRouteExist;
	}
	
	/*
	 * create a new AlgorithmData object (if it doesn't exist already) for the given algorithm type
	 * note that the design of the UI should be such that this is only ever called once:
	 * so: if (<algorithm>Data == null) will always evaluate to true, except for if the first algorithm we
	 * have requested to calculate is not A*, but A* will have been called without alerting the user in order
	 * to find out if there is a posssible route
	 */
	protected AlgorithmStatistics createAlgorithmData(AlgorithmType algorithmType) {
		//FOR NOW I WILL RE-GENERATE A NEW GRAPH FROM THE MAP, but I need to implement a graph cloning algorithm cos this will take to long
		Graph graph = GraphGenerator.generateGraph(map);
		AlgorithmStatistics algorithmStatistics;
		switch (algorithmType) {
		case AStar:
			if(aStarData == null) {
				aStarData = new AlgorithmData(AlgorithmType.AStar, graph);
			}
			algorithmStatistics = new AlgorithmStatistics(aStarData);
			break;
		case AStarSmoothed: 
			if(aStarSmoothedData == null) {
				aStarSmoothedData = new AlgorithmData(AlgorithmType.AStarSmoothed, graph);
			}
			algorithmStatistics = new AlgorithmStatistics(aStarSmoothedData);
			break;
		case ThetaStar:
			if(thetaStarData == null) {
				thetaStarData = new AlgorithmData(AlgorithmType.ThetaStar, graph);
			}
			algorithmStatistics = new AlgorithmStatistics(thetaStarData);
			break;
		default:
			//TODO new algorithms	
			algorithmStatistics = null;
		}
		return algorithmStatistics;	
	}
	
	/*
	 * return the goal node for the given algorithm type
	 */
	protected Node getGoalNode(AlgorithmType algorithmType) {
		Node goal;
		switch (algorithmType) {
		case AStar:
			goal = aStarData.getGoalNode();
			break;
		case AStarSmoothed:
			goal = aStarSmoothedData.getGoalNode();
			break;
		case ThetaStar:
			goal = thetaStarData.getGoalNode();
			break;
		default:
			//TODO new algorithms	
			goal = null;	
		}
		return goal;
	}

}
