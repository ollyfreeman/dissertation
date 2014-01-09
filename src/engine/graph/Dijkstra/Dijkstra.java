package engine.graph.Dijkstra;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import utility.Pair;
import engine.AlgorithmData;
import engine.graph.Graph;
import engine.graph.Node;
import engine.map.Map;

public class Dijkstra extends AlgorithmData {
	
	private static final long serialVersionUID = 1L;
	
	public Dijkstra() {
		super();
	}

	@Override
	public Pair<Node,Integer> getPath(Graph graph, Map map) {
		List<Node> closedSet = new LinkedList<Node>();	
		PriorityQueue<Node> openSet = new PriorityQueue<Node>();
		Node start = graph.getSource();
		initialise(start); 	
		openSet.add(start);
		Node goal = graph.getGoal();
		int nodesExpanded = 0;
		
		while(!openSet.isEmpty()) {
			Node current = openSet.remove();		nodesExpanded++;
			setNode(current,closedSet,map);								//for Lazy Theta Star
			if(goalTest(current,goal,map)) {								//for A* and derivatives
				return new Pair<Node,Integer>(current,nodesExpanded);
			}
			closedSet.add(current);
			for(Node neighbour : current.getNeighbours()) {
				if(!closedSet.contains(neighbour)) {
					 if(updateCost(current, neighbour, goal,map)) {	
						 if(openSet.contains(neighbour)) {				//note these next 3 lines - to account for Java's PriorityQueue being slow to update
							 openSet.remove(neighbour);
						 }
						 openSet.add(neighbour);
					 }
				}
			}
		}
		if(goal.getParent() == null && (!goal.getCoordinate().equals(start.getCoordinate()))) {
			return new Pair<Node,Integer>(null,nodesExpanded);
		} else {
			return new Pair<Node,Integer>(goal,nodesExpanded);
		}
	}

	//note that I'm using "F" scores because my nodes are prioritised on f-scores, but we actually call these g-scores
	protected boolean updateCost(Node current, Node neighbour, Node goal, Map map) {
		double prosposedNewFScore = current.getF() + getDistance(current, neighbour);
		if(prosposedNewFScore < neighbour.getF()) {
			neighbour.setParent(current);
			neighbour.setF(prosposedNewFScore);
			return true;
		} else {
			return false;
		}
	}
	
	protected void initialise(Node start) {
		start.setF(0.0);
	}

	protected void setNode(Node current, List<Node> closedSet, Map map) {
		//Do nothing
	}
	
	protected boolean goalTest(Node current, Node goal, Map map) {
		return false;
	}

}
