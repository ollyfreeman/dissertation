package engine;

import java.util.LinkedList;
import java.util.List;

import engine.graph.*;
import engine.graph.AStar.AStarAlgorithm;
import engine.graph.AStar.AStarSmoothedAlgorithm;
import engine.graph.BlockAStar.BlockAStarAlgorithm;
import engine.graph.BlockAStar.LDDB.raw.BlockAStarAlgorithm_raw;
import engine.graph.Dijkstra.DijkstraAlgorithm;
import engine.graph.ThetaStar.LazyThetaStarAlgorithm;
import engine.graph.ThetaStar.ThetaStarAlgorithm;
import engine.map.Map;
import data.AlgorithmType;

import javax.vecmath.Vector2d;

import utility.Coordinate;

/*
 * Objects of this class contain all of the data relevant to a single running of
 * an algorithm over a map. The graph is a copy made purely for this algorithm on 
 * its associated map.
 */
public class AlgorithmData_Static implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private final AlgorithmType algorithmType;
	private transient final Node goalNode;
	private final Coordinate source;
	private final Coordinate goal;

	/*
	 * these 3 are all redundant (i.e. can be calculated from the Graph and Node),
	 * but certainly worth being cached
	 */
	private final double distance;
	private final double angle;
	private final double time;
	private final List<Coordinate> path;

	public AlgorithmData_Static(AlgorithmType algorithmType, Graph graph, Map map) {
		this.algorithmType = algorithmType;
		this.source = graph.getSource().getCoordinate();
		this.goal = graph.getGoal().getCoordinate();

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
			goalNode = AStarAlgorithm.getPath(graph,map);
			endTime = System.nanoTime();        
			break;
		case AStarSmoothed:
			startTime = System.nanoTime();
			goalNode = AStarAlgorithm.getPath(graph,map);
			AStarSmoothedAlgorithm.smoothe_edge(goalNode,map);
			endTime = System.nanoTime();
			break;
		case ThetaStar:
			startTime = System.nanoTime();
			goalNode = ThetaStarAlgorithm.getPath(graph,map);
			endTime = System.nanoTime();
			break;
		case LazyThetaStar:
			startTime = System.nanoTime();
			goalNode = LazyThetaStarAlgorithm.getPath(graph,map);
			endTime = System.nanoTime();
			break;
		case BlockAStar:
			startTime = System.nanoTime();
			goalNode = BlockAStarAlgorithm.getPath(graph,map);
			endTime = System.nanoTime();
			break;
		case AStarVisibility:
			startTime = System.nanoTime();
			goalNode = AStarAlgorithm.getPath(graph,map);
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
		 Vector2d v0 = new Vector2d(n.getX()-p.getX(), n.getY()-p.getY());
		 Vector2d v1 = new Vector2d(p.getX()-pp.getX(), p.getY()-pp.getY());
		 return v0.angle(v1)*(180.00/Math.PI);
	 }

	 public Coordinate getSource() {
		 return source;
	 }
	 public Coordinate getGoal() {
		 return goal;
	 }


}