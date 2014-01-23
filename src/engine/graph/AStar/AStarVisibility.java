package engine.graph.AStar;

import utility.Coordinate;
import engine.graph.GraphGenerator;
import engine.graph.Node;
import engine.graph.Dijkstra.Dijkstra;
import engine.map.Map;

public class AStarVisibility extends AStar {

	private static final long serialVersionUID = 1L;
	
	public AStarVisibility() {
		super();
	}
	
	public AStarVisibility(Map map, Coordinate source, Coordinate goal) {
		super();
		double startTime = System.nanoTime();
		this.graph =  GraphGenerator.generateGraph_visibility_edge_zeroWidth(map, new Node(source), new Node(goal));
		double endTime = System.nanoTime();
		this.graphCreationTime = (endTime - startTime)/1000000;
		
	}
	
	
	@Override
	protected boolean updateCost(Node current, Node neighbour, Node goal, Map map) {
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
	
	@Override
	protected void initialise(Node start) {
		start.setG(0.0);
	}
	
	@Override
	protected boolean goalTest(Node current, Node goal, Map map) {
		return current.getCoordinate().equals(goal.getCoordinate());
	}
	
}
