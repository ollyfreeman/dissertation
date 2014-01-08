package engine.graph.AStar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import engine.graph.Graph;
import engine.graph.LineOfSight;
import engine.graph.Node;
import engine.map.Map;
import utility.Coordinate;
import utility.Pair;

public class AStarAlgorithm {
	
	private static int counter;
	
	public static Node getPath(Graph graph, Map map) { 
		counter=0;
		List<Node> closedSet = new LinkedList<Node>();				//see line 27 - the closed set isn't needed for functionality, but helps speed up the code - n.b. closed set != Universe \ OpenSet
		PriorityQueue<Node> openSet = new PriorityQueue<Node>();
		Node start = graph.getSource();
		//start.setF(0.0); don't need to set this for the first node
		start.setG(0.0);
		openSet.add(start);
		Node goal = graph.getGoal();
		
		while(!openSet.isEmpty()) {
			Node current = openSet.remove();
			counter++;
			if(current.getCoordinate().equals(goal.getCoordinate())) {
				return goal;
			}
			closedSet.add(current);
			for(Node neighbour : current.getNeighbours()) {
				if(!closedSet.contains(neighbour)) {
					//The previous line: if(!closedSet.contains(neighbour)) { - doesn't affect outcome, but does improve speed
					//On one vis graph i've seen the above line making a difference to outcome :/ !!
					/*next 4 lines are 'updateVertex', also I have renamed 'calculateCost' as 'updateCost'
					 * and made it return a boolean to avoids lines 20&21 of the Theta* article pseudocode
					 * of A*
					 */
					if (updateCost(current, neighbour, goal,map)) {	
						if(openSet.contains(neighbour)){
							openSet.remove(neighbour);
						}
						openSet.add(neighbour);
					}
					/*Used to use the below but the Java priority queue doesn't update quick enough
					 * if (updateCost(current, neighbour, goal,map)) {	
						if(!openSet.contains(neighbour)){
							openSet.add(neighbour);
						}
					}*/
					/*Before the above I used the below - din't change result but resulted in loads of extra expansions duh!
					 * updateCost(current,neighbour,goal,map);
					if(!openSet.contains(neighbour)){
						openSet.add(neighbour);
					}*/
				}
			}
		}
		return null;
	}
	
	private static boolean updateCost(Node current, Node neighbour, Node goal, Map map) {
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
	/* As per my comments in getPath, updateCost can be simplified to the below if we alter
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
	
	//for traceback in BlockAStar
	public static Pair<Double,ArrayList<Coordinate>> getLengthAndIntermediateNodes(Graph g, Map m) {
		Node n = getPath(g,m);
		Node n1 = n;
		
		double distanceAccumulator = 0.0;
		if(n == null) {
			distanceAccumulator = -1;
		} else {
			while(n != null) {
				try {
					distanceAccumulator+=getDistance(n,n.getParent());
				} catch (NullPointerException e) {}
				n = n.getParent();
			}
		}
		ArrayList<Coordinate> list = new ArrayList<Coordinate>();
		if(n1 != null) {
			while(n1.getParent()!=null) {
				list.add(n1.getParent().getCoordinate());
				n1 = n1.getParent();
			}
		}
		if(list.size()!=0) {
			list.remove(list.size()-1);
		}
		return new Pair(distanceAccumulator,list);
	}

}