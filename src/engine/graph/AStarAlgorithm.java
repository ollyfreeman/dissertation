package engine.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class AStarAlgorithm {
	
	public static Node getPath(Graph graph) { 
		
		List<Node> closedSet = new LinkedList<Node>();				//see line 27 - the closed set isn't needed for functionality, but helps speed up the code - n.b. closed set != Universe \ OpenSet
		PriorityQueue<Node> openSet = new PriorityQueue<Node>();
		Node start = graph.getSource();
		//start.setF(0.0); don't need to set this for the first node
		start.setG(0.0);
		openSet.add(start);
		Node goal = graph.getGoal();
		
		while(!openSet.isEmpty()) {
			Node current = openSet.remove();
			if(current == goal) {
				return goal;
			}
			closedSet.add(current);
			for(Node neighbour : current.getNeighbours()) {
				if(!closedSet.contains(neighbour)) {// && fScore >= neighbour.getF()) { 
					//The previous line: if(!closedSet.contains(neighbour)) { - doesn't affect outcome, but does improve speed
					/*next 4 lines are 'updateVertex', also I have renamed 'calculateCost' as 'updateCost'
					 * and made it return a boolean to avoids lines 20&21 of the Theta* article pseudocode
					 * of A*
					 */
					if (updateCost(current, neighbour, goal)) {	
						if(!openSet.contains(neighbour) && !closedSet.contains(neighbour)) {
							openSet.add(neighbour);
						}
					}
					/* n.b. this only happens the first time the node is visited (i.e. expanded) 
					 * because of monotonic heuristic- so this whole block could be replaced with either of the
					 * suggestions below. Also - updateCost can be siplified because of this
					 * if(!openSet.contains(neighbour) && !closedSet.contains(neighbour)) {
					 *		updateCost(current, neighbour, goal);	
					 *		openSet.add(neighbour);
					 * }
					 * or
					 * if(neighbour.getG() == POSITIVE_INFINITY;) {
					 *		updateCost(current, neighbour, goal);	
					 *		openSet.add(neighbour);
					 * }
					 */
				}
			}
		}
		return null;
	}
	
	private static boolean updateCost(Node current, Node neighbour, Node goal) {
		double prosposedNewGScore = current.getG() + getDistance(current, neighbour);
		if(prosposedNewGScore < neighbour.getG()) {
			neighbour.setParent(current);
			neighbour.setG(prosposedNewGScore);
			neighbour.setF(prosposedNewGScore + getDistance(neighbour,goal));
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