package engine.graph.ThetaStar;

import utility.Coordinate;
import engine.graph.LineOfSight;
import engine.graph.Node;
import engine.graph.AStar.AStar;
import engine.map.Map;

public class ThetaStar extends AStar{

	private static final long serialVersionUID = 1L;

	public ThetaStar() {
		super();
	}
	
	public ThetaStar (Map map, Coordinate source, Coordinate goal) {
		super(map,source,goal);
	}
	
	@Override
	protected boolean updateCost(Node current, Node neighbour, Node goal, Map map) {
		double prosposedNewGScore;
		Node parentOfCurrent = current.getParent();
		if(LineOfSight.isVisible_edge_zeroWidth(parentOfCurrent, neighbour, map,false)) {
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

}
