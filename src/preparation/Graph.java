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

}
