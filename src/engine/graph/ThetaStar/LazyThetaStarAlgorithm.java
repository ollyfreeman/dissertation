package engine.graph.ThetaStar;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import engine.graph.Graph;
import engine.graph.LineOfSight;
import engine.graph.Node;
import engine.map.Map;

//NB MOST OF THIS IS COPY AND PASTED FROM AStarAlgorithm, but I've deleted the comments etc
//The only functional difference is in update cost

public class LazyThetaStarAlgorithm {
	
	public static Node getPath(Graph graph, Map map) { 
		
		List<Node> closedSet = new LinkedList<Node>();
		PriorityQueue<Node> openSet = new PriorityQueue<Node>();
		Node start = graph.getSource();
		openSet.add(start);
		start.setG(0.0);
		Node goal = graph.getGoal();
		
		while(!openSet.isEmpty()) {
			Node current = openSet.remove();
			setNode(current, map, closedSet);
			if(current.equals(goal)) {
				return goal;
			}
			closedSet.add(current);
			for(Node neighbour : current.getNeighbours()) {
				if(!closedSet.contains(neighbour)) {
					if (updateCost(current, neighbour, goal, map)) {
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
		if(parentOfCurrent == null) {	//i.e. for start node
			prosposedNewGScore = getDistance(current, neighbour); //+current.getG() - but this should be 0
			neighbour.setParent(current);
			neighbour.setG(prosposedNewGScore);
			neighbour.setF(prosposedNewGScore + getDistance(neighbour,goal));
			return true;
		} else {
			prosposedNewGScore = parentOfCurrent.getG() + getDistance(parentOfCurrent, neighbour);
			if(prosposedNewGScore < neighbour.getG()) {
				neighbour.setParent(parentOfCurrent);
				//System.out.println(parentOfCurrent.coordinateAsString() + " is now parent of " + neighbour.coordinateAsString());
				neighbour.setG(prosposedNewGScore);
				neighbour.setF(prosposedNewGScore + getDistance(neighbour,goal));
				return true;
			} else {
				return false;
			}
		}
	}
	
	private static void setNode(Node current, Map map, List<Node> closedSet) {
		if(current.getParent()!=null) {
			if(!LineOfSight.isVisible_edge_zeroWidth(current.getParent(), current, map)) {
				Node bestNeighbour = null;
				double lowestScore = Double.POSITIVE_INFINITY;
				for(Node n : current.getNeighbours()) {
					double score = n.getG()+getDistance(current,n);
					if(score < lowestScore && closedSet.contains(n)) {
						//System.out.println("Updating" + current.coordinateAsString());
						bestNeighbour = n;
						lowestScore = score;
					}
				}
				if(bestNeighbour==null){
					System.out.println("Oops" + current.coordinateAsString());
				}
				current.setParent(bestNeighbour);
				current.setG(lowestScore);
			}
		}
	}
	
	private static double getDistance(Node n1, Node n2) {
		double xDiff = n1.getX() - n2.getX();
		double yDiff = n1.getY() - n2.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}

}
