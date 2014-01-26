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
import engine.graph.AStar.*;
import engine.graph.BlockAStar.*;
import engine.graph.Dijkstra.Dijkstra;
import engine.graph.ThetaStar.*;

/*
 * this class represents all of the data about a particular map
 * it contains the map itself, data about 
 */
public class MapInstance implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final Map map;
	//private Graph graph; //other graphs are cloned from this - BUT not implemented graph cloning yet
	
	private DoesRouteExist doesRouteExist;
	
	private AlgorithmData dijkstraData;
	private AlgorithmData aStarData;
	private AlgorithmData aStarVisibilityData;
	private AlgorithmData aStarSmoothedData;
	private AlgorithmData thetaStarData;
	private AlgorithmData lazyThetaStarData;
	private AlgorithmData blockAStarData;
	
	protected MapInstance(Map map) {
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
	protected AlgorithmStatistics createAlgorithmData(AlgorithmType algorithmType, Coordinate source, Coordinate goal, boolean isTesting) {
		//FOR NOW I WILL RE-GENERATE A NEW GRAPH FROM THE MAP, but I need to implement a graph cloning algorithm cos this will take to long
		//if I have loaded the graph it will have a null graph instance, so will need to consider this before cloning
		AlgorithmStatistics algorithmStatistics;
		if(algorithmType.equals(AlgorithmType.AStarVisibility)) {
			Graph graph = GraphGenerator.generateGraph_visibility_edge_zeroWidth(map, new Node(source), new Node(goal));
			if(aStarVisibilityData == null) {
				aStarVisibilityData = isTesting ? new AStarVisibility(map,source,goal) : new AStarVisibility();
				aStarVisibilityData.go(graph, map);
			}
			algorithmStatistics = new AlgorithmStatistics(aStarVisibilityData);
			return algorithmStatistics;
		} else {
			Graph graph = GraphGenerator.generateGraph_edge_zeroWidth(map, new Node(source), new Node(goal));
			switch (algorithmType) {
			case Dijkstra:
				if(dijkstraData == null) {
					dijkstraData = isTesting ? new Dijkstra(map,source,goal): new Dijkstra();
					dijkstraData.go(graph, map);
				}
				algorithmStatistics = new AlgorithmStatistics(dijkstraData);
				break;
			case AStar:
				if(aStarData == null) {
					aStarData = isTesting ? new AStar(map,source,goal) : new AStar();
					aStarData.go(graph, map);
				}
				algorithmStatistics = new AlgorithmStatistics(aStarData);
				break;
			case AStarSmoothed: 
				if(aStarSmoothedData == null) {
					aStarSmoothedData = isTesting? new AStarSmoothed(map,source,goal) : new AStarSmoothed();
					aStarSmoothedData.go(graph, map);
				}
				algorithmStatistics = new AlgorithmStatistics(aStarSmoothedData);
				break;
			case ThetaStar:
				if(thetaStarData == null) {
					thetaStarData = isTesting ? new ThetaStar(map,source,goal) : new ThetaStar();
					thetaStarData.go(graph,map);
				}
				algorithmStatistics = new AlgorithmStatistics(thetaStarData);
				break;
			case LazyThetaStar:
				if(lazyThetaStarData == null) {
					lazyThetaStarData = isTesting ? new LazyThetaStar(map,source,goal) : new LazyThetaStar();
					lazyThetaStarData.go(graph,map);
				}
				algorithmStatistics = new AlgorithmStatistics(lazyThetaStarData);
				break;
			case BlockAStar:
				if(blockAStarData == null) {
					BlockAStar_full_halved_rotSymm.loadDB("fullSChalvedRot");
					blockAStarData =  new BlockAStar_full_halved_rotSymm(map,source,goal);
					blockAStarData.go(graph,map);
				}
				algorithmStatistics = new AlgorithmStatistics(blockAStarData);
				break;
			default:
				//TODO new algorithms	
				algorithmStatistics = null;
			}
			graph = null;			// to try to ensure results aren't affected by previous algorithms run on this map instance
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
			case AStarVisibility:
				path = aStarVisibilityData.getPath();
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
	
	protected boolean[][] getNodesExpandedArray(AlgorithmType algorithmType) {
		boolean[][] nea;
		try {
			switch (algorithmType) {
			case Dijkstra :
				nea = dijkstraData.getNodesExpandedArray();
				break;
			case AStar:
				nea = aStarData.getNodesExpandedArray();
				break;
			case AStarVisibility:
				nea = aStarVisibilityData.getNodesExpandedArray();
				break;
			case AStarSmoothed:
				nea = aStarSmoothedData.getNodesExpandedArray();
				break;
			case ThetaStar:
				nea = thetaStarData.getNodesExpandedArray();
				break;
			case LazyThetaStar:
				nea = lazyThetaStarData.getNodesExpandedArray();
				break;
			case BlockAStar:
				nea = blockAStarData.getNodesExpandedArray();
				break;
			default:
				//TODO new algorithms	
				nea = null;	
				System.out.println("MapInstance.getPath(): alg type not found");
			}
		} catch (NullPointerException e) {
			nea = null;	
			System.out.println("MapInstance.getPath(): no path of that type");
		}
		return nea;
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
        case LazyThetaStar:
        		return lazyThetaStarData;
        case BlockAStar:
                return blockAStarData;
        case AStarVisibility:
				return aStarVisibilityData;
        default:
                return null;
        }
	}
}