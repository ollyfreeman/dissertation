package preparation;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class AStarAlgorithm {
	
	public static Node getPath(Graph graph) { 
		
		System.out.println("Starting search");
		//initialise
		List<Node> closedSet = new LinkedList<Node>();
		PriorityQueue<Node> openSet = new PriorityQueue<Node>();
		Node start = graph.getHead();
		openSet.add(start);
		start.setF(0.0);
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
				double gScore = current.getG() + getDistance(current, neighbour);
				double fScore = gScore + getDistance(neighbour,goal);
				if(closedSet.contains(neighbour)) {// && fScore >= neighbour.getF()) { 
					continue;
					/*I thought the whole point was the monotonicity ensured that any route to a 
					 * node was the fastest, so there is never any point re-evaluating a node? so why 
					 * the && bit??
					 * YES OLLY, I THINK YOU'RE RIGHT!
					 */
				}
				if((!openSet.contains(neighbour) || gScore < neighbour.getG())) { 
					System.out.println("gScore: " + gScore + ", neighbour.getG():" + neighbour.getG());
					/*
					 * Before, I had: if(!(openSet.contains(neighbour) || gScore < neighbour.getG())) {
					 * with uninitialised g values (i.e. always 0) and this seemed to work at equvialent
					 * speed or maybe quicker than what I have now! How? Were the results sub-optimal?!
					 */ 
					neighbour.setPrevious(current);
					neighbour.setG(gScore);
					neighbour.setF(fScore);
					if(!openSet.contains(neighbour)) {
						openSet.add(neighbour);
					} 
				}
			}
		}
		System.out.println("No path");
		return null;
	}
	
	private static double getDistance(Node n1, Node n2) {
		double xDiff = n1.getX() - n2.getX();
		double yDiff = n1.getY() - n2.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}

}
