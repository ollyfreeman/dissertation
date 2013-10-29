package preparation;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class AStarAlgorithm_v2 {
	
	public static Node getPath(Graph graph) { 
		
		System.out.println("Starting search");
		List<Node> closedSet = new LinkedList<Node>();
		PriorityQueue<Node> openSet = new PriorityQueue<Node>();
		Node start = graph.getHead();
		openSet.add(start);
		//start.setF(0.0); don't need to set this for the first node
		start.setG(0.0);
		Node goal = graph.getGoal();
		
		while(!openSet.isEmpty()) {
			Node current = openSet.remove();
			if(current == goal) {
				System.out.println("Found path");
				return goal;
			}
			closedSet.add(current);
			for(Node neighbour : current.getSuccessors()) {
				//double gScore = current.getG() + getDistance(current, neighbour);
				//double fScore = gScore + getDistance(neighbour,goal);
				if(!closedSet.contains(neighbour)) {// && fScore >= neighbour.getF()) { 
					/*next 4 lines are 'updateVertex', also I have renamed 'calculateCost' as 'updateCost'
					 * and made it return a boolean to avoids lines 20&21 of the Theta* article pseudocode
					 * of A*
					 */
					if (updateCost(current, neighbour, goal)) {
						if(!openSet.contains(neighbour)) {
							openSet.add(neighbour);
						}
					}
				}
			}
		}
		System.out.println("No path");
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
	
	private static double getDistance(Node n1, Node n2) {
		double xDiff = n1.getX() - n2.getX();
		double yDiff = n1.getY() - n2.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}

}
