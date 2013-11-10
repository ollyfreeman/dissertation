package engine;

import engine.graph.*;
import data.AlgorithmType;

public class AlgorithmData {
	
	private final AlgorithmType algorithmType;
	private final Graph graph;
	private final Node goalNode;
	
	private double distance;	//these 3 are all redundant, in that they could be derived from the graph, but would be silly to not keep them here
	private double angle;
	private final double time;
	
	public AlgorithmData(AlgorithmType algorithmType, Graph graph) {
		super();
		this.algorithmType = algorithmType;
		this.graph = graph;
		double startTime, endTime;
		if(algorithmType.equals(AlgorithmType.AStar)) {
			startTime = System.nanoTime();
			goalNode = AStarAlgorithm.getPath(graph);
			endTime = System.nanoTime();
			
		} else if(algorithmType.equals(AlgorithmType.AStarSmoothed)) {
			startTime = System.nanoTime();
			goalNode = AStarAlgorithm.getPath(graph);
			AStarSmoothed.smoothe(graph.getHead(), goalNode);
			endTime = System.nanoTime();
		} else if(algorithmType.equals(AlgorithmType.ThetaStar)) {
			startTime = System.nanoTime();
			goalNode = ThetaStarAlgorithm.getPath(graph);
			endTime = System.nanoTime();
		} else { //need to fill the else clauses properly
			startTime =  0.0;
			endTime = 0.0;
			goalNode = null;
			System.out.println("Oops");
		}
		time = (endTime - startTime)/1000000;
		Node n = goalNode;
		while(n != null) {
			try {
				distance+=getDistance(n,n.getParent());
			} catch (NullPointerException e) {
				//when we get to the final (source) node
			}
			try {
				angle += getAngle(n);
			}catch (NullPointerException e) {
				//when we get to the penultimate node
			}
			n = n.getParent();
		}
		
	}
	
	public AlgorithmType getAlgorithmType() {
		return algorithmType;
	}

	public Graph getGraph() {
		return graph;
	}

	public Node getGoalNode() {
		return goalNode;
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
	
	private double getDistance(Node n1, Node n2) {
		double xDiff = n1.getX() - n2.getX();
		double yDiff = n1.getY() - n2.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}
	
	private double getAngle(Node n) {
		Node p = n.getParent();
		Node pp = p.getParent();
		double dotProduct = (p.getX()-n.getX())*(pp.getX()-p.getX()) + (p.getY()-n.getY())*(pp.getY()-p.getY());
		double denominator = getDistance(n, p)*getDistance(p, pp);
		double cosAngle = (dotProduct/denominator);
		if(cosAngle > 1) {
			System.out.println("Nodes: " + n.toString() + ", " + p.toString() + " and " + pp.toString() + " gave an cos of angle of " + cosAngle);
		}
		double angle = (Math.acos(cosAngle))*(180/Math.PI);
		return angle; //abs returns a value between
	}

}
