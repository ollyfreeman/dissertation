package engine.graph.AStar;

import engine.graph.Node;
import engine.graph.Dijkstra.Dijkstra;
import engine.map.Map;

public class AStar extends Dijkstra {

	private static final long serialVersionUID = 1L;
	
	public AStar() {
		super();
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
