package engine.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class DijkstraAlgorithm {
	
	public static Node getPath(Graph graph) { 
		
		//I'm using "f" as g here so that I can use Node class which prioritises on F-score
		
		List<Node> closedSet = new LinkedList<Node>();	
		PriorityQueue<Node> openSet = new PriorityQueue<Node>();
		Node start = graph.getSource();
		start.setF(0.0);
		openSet.add(start);
		Node goal = graph.getGoal();
		
		while(!openSet.isEmpty()) {
			Node current = openSet.remove();
			closedSet.add(current);
			for(Node neighbour : current.getNeighbours()) {
				if(!closedSet.contains(neighbour)) {
					 updateCost(current, neighbour, goal);	
					 if(!openSet.contains(neighbour)) {
						 openSet.add(neighbour);
					 }
				}
			}
		}
		if(goal.getParent() == null) {
			return null;
		} else {
			return goal;
		}
	}
	
	//remember I'm using "f-score" as the Dij score even though it's really a g-score - this is only because my Nodes are sorting on F-scores
	private static boolean updateCost(Node current, Node neighbour, Node goal) {
		double prosposedNewFScore = current.getF() + getDistance(current, neighbour);
		if(prosposedNewFScore < neighbour.getF()) {
			neighbour.setParent(current);
			neighbour.setF(prosposedNewFScore);
			return true;
		} else {
			return false;
		}
	}
	/* According to comments in getPath, updateCost can be simplified to the below if we alter
	 * the code in getPath accordingly:
	 * 
	 * private static void updateCost(Node current, Node neighbour, Node goal) {
 	 * 		neighbour.setParent(current);
	 *  	double newGScore = current.getG() + getDistance(current, neighbour);
	 *  	neighbour.setG(newGScore);
	 *  	neighbour.setF(neighbour.getG() + getDistance(neighbour,goal));
	 * }
	 * 
	 */
	
	/*
	 * helper method to calculate Euclidean distance
	 */
	private static double getDistance(Node n1, Node n2) {
		double xDiff = n1.getX() - n2.getX();
		double yDiff = n1.getY() - n2.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}

}