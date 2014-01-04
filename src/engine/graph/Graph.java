package engine.graph;

import java.util.HashMap;

import utility.Coordinate;

public class Graph implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//Should we keep statistics about how long it took to create each graph, for later evaluation?
	
	//Later I may need to subclass this as GridGraph and VisibilityGraph
	
	private HashMap<Coordinate,Node> graph;
	private Node source;
	private Node goal;
	
	//should really use Factory pattern on this and Map classes
	public Graph() {
		this.graph = new HashMap<Coordinate,Node>();
	}
	
	public void addNode(Node node) {
		graph.put(node.getCoordinate(), node);
	}
	
	//returns the START/SOURCE node of the path
	public Node getSource() {
		return this.source;
	}
	
	public void setSource(Node source) {
		this.source = source;
	}
	
	//returns the GOAL/SINK node of the path
	public Node getGoal() {
		return goal;
	}
	
	public void setGoal(Node goal) {
		this.goal = goal;
	}
	
	public Node getNode(Coordinate c) {
		return graph.get(c);
	}
	
	public boolean doesEdgeExist(Coordinate c1, Coordinate c2) {
		Node n1 = getNode(c1);
		if(n1 != null) {
			return (n1.getNeighbourIfExists(c2.getX(), c2.getY()) != null);
		} else {
			return false;
		}
	}
	
	public Graph cloneMe() {
		/*
		 * may need to implement this later to avoid having to recompute the graph from the map each time for each algorithm
		 * because of all the interdependencies between nodes, will need to create them all first and then go through and set
		 * parents and neighbours
		 */
		return null;
	}
	
	//DELETE ME
	public HashMap<Coordinate,Node> getHashMap() {
		return graph;
	}

}
