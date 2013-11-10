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
	
	private DoesRouteExist doesRouteExist = null;
	
	private AlgorithmData aStarData;
	private AlgorithmData aStarSmoothedData;
	private AlgorithmData thetaStarData;
	
	public MapInstance(Map map) {
		this.map = map;
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
		if(this.doesRouteExist == null) {
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
	
	protected Node getGoalNode(AlgorithmType algorithmType) {
		switch (algorithmType) {
		case AStar:
			return aStarData.getGoalNode();
		case AStarSmoothed:
			return aStarSmoothedData.getGoalNode();
		case ThetaStar:
			return thetaStarData.getGoalNode();
		default:
			//TODO new algorithms	
			return null;	
		}
	}

}
