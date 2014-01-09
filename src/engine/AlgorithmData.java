package engine;

import java.util.LinkedList;

import engine.graph.*;
import engine.map.Map;

import javax.vecmath.Vector2d;

import utility.Coordinate;
import utility.Pair;

/*
 * Objects of this class contain all of the data relevant to a single running of
 * an algorithm over a map. The graph is a copy made purely for this algorithm on 
 * its associated map.
 */
public abstract class AlgorithmData implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private transient Node goalNode;
	private Coordinate source;
	private Coordinate goal;
	
	/*
	 * these 3 are all redundant (i.e. can be calculated from the Graph and Node),
	 * but certainly worth being cached
	 */
	private double distance;
	private double angle;
	private double time;
	private int nodesExpanded;
	private LinkedList<Coordinate> path;
	
	public AlgorithmData() {
	}
	
	public void go(Graph graph, Map map) {
		this.source = graph.getSource().getCoordinate();
		this.goal = graph.getGoal().getCoordinate();
		Pair<Node,Integer> p0;
		double startTime = System.nanoTime();
		p0  = this.getPath(graph, map);
		double endTime = System.nanoTime();	
		this.goalNode = p0.get0();
		this.nodesExpanded = p0.get1();
		System.out.println("Nodes Expanded "+nodesExpanded);
		this.time = (endTime - startTime)/1000000;
		
		Pair<Pair<Double,Double>,LinkedList<Coordinate>> p1 = calculateDistanceAnglePath();
		this.distance = p1.get0().get0();
		this.angle = p1.get0().get1();
		this.path = p1.get1();
		System.out.println("Done");
	}
	
	/*
	 * returns the number of nodes expanded during execution
	 */
	protected abstract Pair<Node,Integer> getPath(Graph graph, Map map);

	private Pair<Pair<Double,Double>,LinkedList<Coordinate>> calculateDistanceAnglePath() {
		Node n = goalNode;
		double distanceAccumulator = 0.0;
		double angleAccumulator = 0.0;
		LinkedList<Coordinate> path = new LinkedList<Coordinate>();
		while(n != null) {
			try {
				path.add(n.getCoordinate());
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
		Pair<Double,Double> doubles = new Pair<Double,Double>(distanceAccumulator,angleAccumulator);
		return new Pair<Pair<Double,Double>,LinkedList<Coordinate>>(doubles,path);
	}

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
	
	public LinkedList<Coordinate> getPath() {
		return path;
	}
	
	public int getNodesExpanded() {
		return nodesExpanded;
	}
	
	/*
	 * helper method - gets Euclidean distance between 2 nodes
	 */
	protected double getDistance(Node n1, Node n2) {
		double xDiff = n1.getX() - n2.getX();
		double yDiff = n1.getY() - n2.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}
	
	/*
	 * helper method - gets angle between the lines joining a node and its parent, and the node's parent and the node's parent's parent
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
