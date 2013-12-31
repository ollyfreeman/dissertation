package engine.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import engine.map.Map;

//NB MOST OF THIS IS COPY AND PASTED FROM AStarAlgorithm, but I've deleted the comments etc
//The only functional difference is in update cost

public class ThetaStarAlgorithm {
	
	public static Node getPath(Graph graph, Map map) { 
		
		List<Node> closedSet = new LinkedList<Node>();
		PriorityQueue<Node> openSet = new PriorityQueue<Node>();
		Node start = graph.getSource();
		openSet.add(start);
		start.setG(0.0);
		Node goal = graph.getGoal();
		
		while(!openSet.isEmpty()) {
			Node current = openSet.remove();
			if(current == goal) {
				return goal;
			}
			closedSet.add(current);
			for(Node neighbour : current.getNeighbours()) {
				if(!closedSet.contains(neighbour)) {
					if (updateCost(current, neighbour, goal,map)) {
						if(!openSet.contains(neighbour)) {
							openSet.add(neighbour);
						}
					}
				}
			}
		}
		return null;
	}
	
	private static boolean updateCost(Node current, Node neighbour, Node goal, Map map) {
		double prosposedNewGScore;
		Node parentOfCurrent = current.getParent();
		if(LineOfSight.isVisible_edge_finiteWidth(parentOfCurrent, neighbour, map)) {
			prosposedNewGScore = parentOfCurrent.getG() + getDistance(parentOfCurrent, neighbour);
			if(prosposedNewGScore < neighbour.getG()) {
				neighbour.setParent(parentOfCurrent);
				neighbour.setG(prosposedNewGScore);
				neighbour.setF(prosposedNewGScore + getDistance(neighbour,goal));
				return true;
			} else {
				return false;
			}
		}
		else {
			prosposedNewGScore = current.getG() + getDistance(current, neighbour);
			if(prosposedNewGScore < neighbour.getG()) {
				neighbour.setParent(current);
				neighbour.setG(prosposedNewGScore);
				neighbour.setF(prosposedNewGScore + getDistance(neighbour,goal));
				return true;
			} else {
				return false;
			}
		}
	}
	
	private static double getDistance(Node n1, Node n2) {
		double xDiff = n1.getX() - n2.getX();
		double yDiff = n1.getY() - n2.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}

}
