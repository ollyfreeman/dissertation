package preparation;

import java.util.LinkedList;
import java.util.List;

public class Node {

	private List<Node> successors;
	private final Coordinate coordinate;
	
	private double f;
	private double g;
	private Node previous;
	
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
