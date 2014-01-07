package engine;

import java.util.List;

import utility.AlgorithmStatistics;
import utility.Coordinate;
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
public class MapInstance implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final Map map;
	private Graph graph; //other graphs are cloned from this - BUT not implemented graph cloning yet
	
	private DoesRouteExist doesRouteExist;
	
	private AlgorithmData dijkstraData;
	private AlgorithmData aStarData;
	private AlgorithmData aStarVisData;
	private AlgorithmData aStarSmoothedData;
	private AlgorithmData thetaStarData;
	private AlgorithmData lazyThetaStarData;
	private AlgorithmData blockAStarData;
	
	public MapInstance(Map map) {
		this.map = map;
		this.doesRouteExist = DoesRouteExist.DontKnow;
	}
	
	protected Map getMap() {
		return map;
	}
	
	/*
	 * use AStar to determine whether or not a route exists
	 */
	protected DoesRouteExist doesRouteExist() {
		if(!(this.doesRouteExist.equals(DoesRouteExist.No) || this.doesRouteExist.equals(DoesRouteExist.Yes))) {
			if(dijkstraData == null) {
				this.doesRouteExist = DoesRouteExist.DontKnow;
			} else if (!dijkstraData.goalNodeExists()) {
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
	 * to find out if there is a possible route
	 */
	protected AlgorithmStatistics createAlgorithmData(AlgorithmType algorithmType, Coordinate source, Coordinate goal) {
		//FOR NOW I WILL RE-GENERATE A NEW GRAPH FROM THE MAP, but I need to implement a graph cloning algorithm cos this will take to long
		//if I have loaded the graph it will have a null graph instance, so will need to consider this before cloning
		AlgorithmStatistics algorithmStatistics;
		if(algorithmType.equals(AlgorithmType.AStarVis)) {
			Graph graph = GraphGenerator.generateGraph_visibility_edge_zeroWidth(map, new Node(source), new Node(goal));
			if(aStarVisData == null) {
				aStarVisData = new AlgorithmData(AlgorithmType.AStarVis, graph, map);
			}
			algorithmStatistics = new AlgorithmStatistics(aStarVisData);
			return algorithmStatistics;
		} else {
			Graph graph = GraphGenerator.generateGraph_edge_zeroWidth(map, new Node(source), new Node(goal));
			switch (algorithmType) {
			case Dijkstra:
				if(dijkstraData == null) {
					dijkstraData = new AlgorithmData(AlgorithmType.Dijkstra, graph, map);
				}
				algorithmStatistics = new AlgorithmStatistics(dijkstraData);
				break;
			case AStar:
				if(aStarData == null) {
					aStarData = new AlgorithmData(AlgorithmType.AStar, graph, map);
				}
				algorithmStatistics = new AlgorithmStatistics(aStarData);
				break;
			case AStarSmoothed: 
				if(aStarSmoothedData == null) {
					aStarSmoothedData = new AlgorithmData(AlgorithmType.AStarSmoothed, graph, map);
				}
				algorithmStatistics = new AlgorithmStatistics(aStarSmoothedData);
				break;
			case ThetaStar:
				if(thetaStarData == null) {
					thetaStarData = new AlgorithmData(AlgorithmType.ThetaStar, graph,map);
				}
				algorithmStatistics = new AlgorithmStatistics(thetaStarData);
				break;
			case LazyThetaStar:
				if(lazyThetaStarData == null) {
					lazyThetaStarData = new AlgorithmData(AlgorithmType.LazyThetaStar, graph,map);
				}
				algorithmStatistics = new AlgorithmStatistics(lazyThetaStarData);
				break;
			case BlockAStar:
				if(blockAStarData == null) {
					blockAStarData = new AlgorithmData(AlgorithmType.BlockAStar, graph,map);
				}
				algorithmStatistics = new AlgorithmStatistics(blockAStarData);
				break;
			default:
				//TODO new algorithms	
				algorithmStatistics = null;
			}
			return algorithmStatistics;
		}
			
	}
	
	/*
	 * return the goal node for the given algorithm type
	 */
	protected List<Coordinate> getPath(AlgorithmType algorithmType) {
		List<Coordinate> path;
		try {
			switch (algorithmType) {
			case Dijkstra :
				path = dijkstraData.getPath();
				break;
			case AStar:
				path = aStarData.getPath();
				break;
			case AStarVis:
				path = aStarVisData.getPath();
				break;
			case AStarSmoothed:
				path = aStarSmoothedData.getPath();
				break;
			case ThetaStar:
				path = thetaStarData.getPath();
				break;
			case LazyThetaStar:
				path = lazyThetaStarData.getPath();
				break;
			case BlockAStar:
				path = blockAStarData.getPath();
				break;
			default:
				//TODO new algorithms	
				path = null;	
				System.out.println("MapInstance.getPath(): alg type not found");
			}
		} catch (NullPointerException e) {
			path = null;	
			System.out.println("MapInstance.getPath(): no path of that type");
		}
		return path;
	}
	
	protected AlgorithmData getAlgorithmData(AlgorithmType algorithmType) {
        switch (algorithmType) {
        case Dijkstra:
                return dijkstraData;
        case AStar:
                return aStarData;
        case AStarSmoothed: 
                return aStarSmoothedData;
        case ThetaStar:
                return thetaStarData;
        case BlockAStar:
                return blockAStarData;
        default:
                return null;
        }
}

}
