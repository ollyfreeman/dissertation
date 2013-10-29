package preparation;

import java.util.LinkedList;
import java.util.List;

public class Node implements Comparable {

	private List<Node> successors;
	private final Coordinate coordinate;
	
	private double f;
	private double g = Double.POSITIVE_INFINITY;
	private Node parent;
	
	public Node(Coordinate coordinate) {
		successors = new LinkedList<Node>();		//is this the best implementation?
		this.coordinate = coordinate;
	}

	public void addSuccessor(Node node) {
		successors.add(node);
	}
	
	public List<Node> getSuccessors() {
		return successors;
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

	public Node getPrevious() {
		return parent;
	}

	public void setPrevious(Node previous) {
		this.parent = previous;
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
		s+= ", Successors: ";
		for(Node n : successors) {
			s+= n.coordinateAsString() + " ";
		}
		return s;
	}
	
}