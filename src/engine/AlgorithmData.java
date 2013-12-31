package engine;

import java.util.LinkedList;
import java.util.List;

import engine.graph.*;
import engine.map.Map;
import data.AlgorithmType;

import javax.vecmath.Vector2d;

import utility.Coordinate;

/*
 * Objects of this class contain all of the data relevant to a single running of
 * an algorithm over a map. The graph is a copy made purely for this algorithm on 
 * its associated map.
 */
public class AlgorithmData implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final AlgorithmType algorithmType;
	//private transient final Graph graph;
	private transient final Node goalNode;
	
	/*
	 * these 3 are all redundant (i.e. can be calculated from the Graph and Node),
	 * but certainly worth being cached
	 */
	private final double distance;
	private final double angle;
	private final double time;
	private final List<Coordinate> path;
	
	public AlgorithmData(AlgorithmType algorithmType, Graph graph, Map map) {
		this.algorithmType = algorithmType;
		//this.graph = graph;
		
		//I have all of this in the constructor as a lot of the instance variables are final
		/*
		 * Do the algorithm to get the goal node and the time
		 */
		double startTime, endTime;
		switch (algorithmType) {
		case Dijkstra:
			startTime = System.nanoTime();
			goalNode = DijkstraAlgorithm.getPath(graph);
			endTime = System.nanoTime();	
			break;
		case AStar:
			startTime = System.nanoTime();
			goalNode = AStarAlgorithm.getPath(graph);
			endTime = System.nanoTime();	
			break;
		case AStarSmoothed:
			startTime = System.nanoTime();
			goalNode = AStarAlgorithm.getPath(graph);
			AStarSmoothed.smoothe_edge(graph.getSource(), goalNode,map);
			endTime = System.nanoTime();
			break;
		case ThetaStar:
			startTime = System.nanoTime();
			goalNode = ThetaStarAlgorithm.getPath(graph,map);
			endTime = System.nanoTime();
			break;
		default:
			//TODO for the further algorithms
			startTime =  0.0;
			endTime = 0.0;
			goalNode = null;
			System.out.println("Oops");
		}
		time = (endTime - startTime)/1000000;
		
		/*
		 * calculate the distance and angle by tracing back through path from goal to source
		 */
		Node n = goalNode;
		double distanceAccumulator = 0.0;
		double angleAccumulator = 0.0;
		path = new LinkedList<Coordinate>();
		while(n != null) {
			try {
				path.add(0,n.getCoordinate());
				distanceAccumulator+=getDistance(n,n.getParent());
			} catch (NullPointerException e) {
				//when we get to the final (source) node
			}
			try {
				angleAccumulator += getAngle(n);
			}catch (NullPointerException e) {
				//when we get to the penultimate node
			}
			n = n.getParent();
		}
		this.distance = distanceAccumulator;
		this.angle = angleAccumulator;
	}
	
	public AlgorithmType getAlgorithmType() {
		return algorithmType;
	}

	/*
	public Graph getGraph() {
		return graph;
	}
	*/

	public boolean goalNodeExists() {
		return (goalNode != null);
	}

	public double getDistance() {
		return distance;
	}

	public double getAngle() {
		return angle;
	}

	public double getTime() {
		return time;
	}
	
	public List<Coordinate> getPath() {
		return path;
	}
	
	/*
	 * helper method - gets Euclidean distance between 2 nodes
	 */
	private double getDistance(Node n1, Node n2) {
		double xDiff = n1.getX() - n2.getX();
		double yDiff = n1.getY() - n2.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}
	
	/*
	 * helper method - implemented dot product myself in 'preparation.TestAlgorithms'
	 * but have used a library here
	 */
	private double getAngle(Node n) {
		Node p = n.getParent();
		Node pp = p.getParent();
		Vector2d v0 = new Vector2d(p.getX()-n.getX(), p.getY()-n.getY());
		Vector2d v1 = new Vector2d(pp.getX()-p.getX(), pp.getY()-p.getY());
		return v0.angle(v1)*(180.00/Math.PI);
	}

}
