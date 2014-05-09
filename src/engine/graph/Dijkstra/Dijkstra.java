package engine.graph.Dijkstra;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import utility.Coordinate;
import utility.Pair;
import engine.AlgorithmData;
import engine.graph.Graph;
import engine.graph.GraphGenerator;
import engine.graph.Node;
import engine.map.Map;

public class Dijkstra extends AlgorithmData {
	
	private static final long serialVersionUID = 1L;
	
	public int PriLength = 0;
	public int nodeExp = 0;
	
	public Dijkstra() {
		super();
	}
	
	public Dijkstra(Map map, Coordinate source, Coordinate goal) {
		super();
		double startTime = System.nanoTime();
		this.graph =  GraphGenerator.generateGraph_edge_zeroWidth(map, new Node(source), new Node(goal));
		double endTime = System.nanoTime();
		this.graphCreationTime = (endTime - startTime)/1000000;
	}

	@Override
	public Pair<Node,int[][]> getPath(Graph graph, Map map, int[][] nea) {
		HashSet<Node> closedSet = new HashSet<Node>();	
		PriorityQueue<Node> openSet = new PriorityQueue<Node>();
		Node start = graph.getSource();
		initialise(start); 	
		openSet.add(start);
		Node goal = graph.getGoal();
		
		while(!openSet.isEmpty()) {
			nodeExp++;
			PriLength+=openSet.size();
			Node current = openSet.remove();	nea[current.getX()][current.getY()]++;
			//System.out.println(current.coordinateAsString());
			
			setNode(current,closedSet,map);									//for Lazy Theta Star
			if(goalTest(current,goal,map,nea)) {
				//System.out.println("Nodes expanded : " + nodesExpanded);
				System.out.println(PriLength/nodeExp);
				return new Pair<Node,int[][]>(current,nea);
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
			return new Pair<Node,int[][]>(null,nea);
		} else {
			//System.out.println("Nodes expanded : " + nodesExpanded);
			return new Pair<Node,int[][]>(goal,nea);
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

	protected void setNode(Node current, HashSet<Node> closedSet, Map map) {
		//Do nothing
	}
	
	protected boolean goalTest(Node current, Node goal, Map map, int[][] nea) {
		return (current.getCoordinate().equals(goal.getCoordinate()));
	}

}
