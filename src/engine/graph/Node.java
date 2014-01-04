package engine.graph;

import java.util.LinkedList;
import java.util.List;

import utility.Coordinate;

/*
 * represents the node on a connected graph
 */
public class Node implements Comparable<Node>, java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	private final Coordinate coordinate;			//coordinate of the map Cell that this node represents
	
	private List<Node> neighbours;
	private Node parent;

	private double f;
	private double g;

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

	/*
	 * if this node has a neighbour at the coordinate given, then return that neighbour
	 * else return null
	 */
	public Node getNeighbourIfExists(int x, int y) {
		for(Node n : neighbours) {
			if((n.getX() == x) && (n.getY() == y)) {
				return n;
			}
		}
		return null;
	}

	@Override
	/*
	 * Makes nodes comparable by f-score, so they can be sorted in a priority queue
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Node node) {
		if(this.getF() < node.getF()) {
			return -1;
		} else if(this.getF() > node.getF()) {
			return 1;
		} else {
			return 0;
		}
	}
	
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
	}

}
