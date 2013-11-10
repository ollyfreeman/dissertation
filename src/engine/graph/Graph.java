package engine.graph;

import java.util.LinkedList;
import java.util.List;

public class Graph {
	
	//Should we keep statistics about how long it took to create each graph, for later evaluation?
	
	//Later I may need to subclass this as GridGraph and VisibilityGraph
	
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
	
	//returns the GOAL/SINK node of the path
	public Node getGoal() {
		return goal;
	}
	
	public Graph cloneMe() {
		/*
		 * may need to implement this later to avoid having to recompute the graph from the map each time for each algorithm
		 * because of all the interdependencies between nodes, will need to create them all first and then go through and set
		 * parents and neighbours
		 */
		return null;
	}

}
