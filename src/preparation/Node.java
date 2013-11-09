package preparation;

import java.util.LinkedList;
import java.util.List;

public class Node implements Comparable {

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
	
	//for VisibilityTest stuff with Bresenham
	public Node getReachable(int x, int y) { //should this be Coordinate object?
		/*if((!(parent == null)) && (parent.getX() == x) && (parent.getY() == y)) {
			return parent;
		} else {*/
			for(Node n : neighbours) {
				if((n.getCoordinate().getX() == x) && (n.getCoordinate().getY() == y)) {
					return n;
				}
			}
		//}
		/*System.out.print("Successors of ("+x+","+y+"): ");
		for(Node n : successors) {
			System.out.print("(" + n.getX() + "," + n.getY() + ")");
		}
		System.out.print("... :( ...");*/
		return null;
	}
	
	@Override
	//comparable by f score
	public int compareTo(Object other) {
		Node node = (Node) other;
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
