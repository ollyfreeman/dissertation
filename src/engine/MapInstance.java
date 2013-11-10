package engine;

import utility.AlgorithmStatistics;
import data.AlgorithmType;
import data.DoesRouteExist;
import engine.graph.Graph;
import engine.graph.Node;
import engine.map.Map;
import engine.graph.GraphGenerator;

public class MapInstance {
	
	private final Map map;
	private Graph graph; //other graphs are cloned from this - not implemented graph cloning yet
	
	private DoesRouteExist doesRouteExist;
	
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
	
	//use AStar to determine whether or not a route exists
	protected DoesRouteExist doesRouteExist() {
		if(aStarData == null) {
			this.doesRouteExist = DoesRouteExist.DontKnow;
		} else if (aStarData.getGoalNode() == null) {
			this.doesRouteExist = DoesRouteExist.No;
		} else {
			this.doesRouteExist = DoesRouteExist.Yes;
		}
		return this.doesRouteExist;
	}
	
	protected AlgorithmStatistics createAlgorithmData(AlgorithmType algorithmType) {
		//FOR NOW I WILL RE-GENERATE A NEW GRAPH FROM THE MAP, but I need to implement a graph cloning algorithm cos this will take to long
		Graph graph = GraphGenerator.generateGraph(map);
		if(algorithmType.equals(AlgorithmType.AStar)) {
			if(aStarData == null) {
				aStarData = new AlgorithmData(AlgorithmType.AStar, graph);
			}
			AlgorithmStatistics algorithmStatistics = new AlgorithmStatistics(aStarData);
			return algorithmStatistics;	
			
		} else if(algorithmType.equals(AlgorithmType.AStarSmoothed)) {
			if(aStarSmoothedData == null) {
				aStarSmoothedData = new AlgorithmData(AlgorithmType.AStarSmoothed, graph);
			}
			AlgorithmStatistics algorithmStatistics = new AlgorithmStatistics(aStarSmoothedData);
			return algorithmStatistics;	
			
		} else if(algorithmType.equals(AlgorithmType.ThetaStar)) {
			if(thetaStarData == null) {
				thetaStarData = new AlgorithmData(AlgorithmType.ThetaStar, graph);
			}
			AlgorithmStatistics algorithmStatistics = new AlgorithmStatistics(thetaStarData);
			return algorithmStatistics;	
			
		} else {
			//TODO should never enter here
			System.out.println("Panic!");
			return null;
		}
	}
	
	protected Node getGoalNode(AlgorithmType algorithmType) {
		if(algorithmType.equals(AlgorithmType.AStar)) {
			return aStarData.getGoalNode();
		} else if (algorithmType.equals(AlgorithmType.AStarSmoothed)){
			return aStarSmoothedData.getGoalNode();
		} else if (algorithmType.equals(AlgorithmType.ThetaStar)){
			return thetaStarData.getGoalNode();
		} else {
			//TODO you shouldn't end up here!
			System.out.println("PANIC");
			return null;
		}
	}

}
