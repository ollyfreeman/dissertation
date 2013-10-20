package preparation;

import java.util.LinkedList;
import java.util.List;

public class Graph {
	
	private List<Node> graph;
	
	//should really use Factory pattern on this and Map classes
	public Graph() {
		this.graph = new LinkedList<Node>();
	}
	
	public void addNode(Node node) {
		graph.add(node);
	}
	
	//?? or getList? surely head is best
	public Node getHead() {
		return graph.get(0);
	}

}
