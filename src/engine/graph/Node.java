package engine.graph;

import java.util.LinkedList;
import java.util.List;

import utility.Coordinate;

public class Node implements Comparable<Node> {

	private List<Node> neighbours;
	private final Coordinate coordinate;

	private double f;
	private double g;
	private Node parent;

	public Node(Coordinate coordinate) {
		neighbours = new LinkedList<Node>();		//is this the best implementation?
		this.coordinate = coordinate;
		parent = null;
		g = Double.POSITIVE_INFINITY;
		f = Double.POSITIVE_INFINITY;	//does NEED to be initialised because it's used to sort the priority queue
	}

	public void addNeighbour(Node node) {
		neighbours.add(node);
	}

	public List<Node> getNeighbours() {
		return neighbours;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public int getX() {
		return coordinate.getX();
	}

	public int getY() {
		return coordinate.getY();
	}

	public double getF() {
		return f;
	}

	public void setF(double f) {
		this.f = f;
	}

	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node previous) {
		this.parent = previous;
	}

	public Node getReachable(int x, int y) {
		for(Node n : neighbours) {
			if((n.getCoordinate().getX() == x) && (n.getCoordinate().getY() == y)) {
				return n;
			}
		}
		return null;
	}

	@Override
	//comparable by f score
	public int compareTo(Node other) {
		Node node = (Node) other;
		if(this.getF() < node.getF()) {
			return -1;
		} else if(this.getF() > node.getF()) {
			return 1;
		} else {
			return 0;
		}
	}

	/*
	public String coordinateAsString() {
		return "(" + coordinate.getX() +"," + coordinate.getY() + ")";
	}

	public String toString() {
		String s ="Coordinate: (" + coordinate.getX() +"," + coordinate.getY() + ")";
		s+= ", Neighbours: ";
		for(Node n : neighbours) {
			s+= n.coordinateAsString() + " ";
		}
		return s;
	}*/

}
