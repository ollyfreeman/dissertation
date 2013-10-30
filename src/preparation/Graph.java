package preparation;

import java.util.LinkedList;
import java.util.List;

public class Graph {
	
	private List<Node> graph;
	private Node goal;
	
	//should really use Factory pattern on this and Map classes
	public Graph() {
		this.graph = new LinkedList<Node>();	//why linkedlist? we never traverse this list sequentially? Why not arrayList?
	}
	
	public void addNode(Node node) {
		graph.add(node);
		goal = node;
	}
	
	//returns the START/SOURCE node of the path
	public Node getHead() {
		return graph.get(0);
	}
	
	public Node getGoal() {
		return goal;
	}
	
	//FOR TESTING BRESENHAM PURPOSES ONLY
	public Node get2010() {
		for(Node n: graph) {
			if((n.getCoordinate().getX() == 20) && (n.getCoordinate().getY() == 10)) {
				return n;
			}
		}
		return null;
	}

}
