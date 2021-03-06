package engine.graph.AStar;

import utility.Coordinate;
import engine.graph.LineOfSight;
import engine.graph.Node;
import engine.map.Map;

public class AStarSmoothed extends AStar {

	private static final long serialVersionUID = 1L;
	
	public AStarSmoothed() {
		super();
	}
	
	public AStarSmoothed (Map map, Coordinate source, Coordinate goal) {
		super(map,source,goal);
	}
	
	@Override
	protected boolean goalTest(Node current, Node goal, Map map,int[][] nea) {
		if(!current.getCoordinate().equals(goal.getCoordinate())) {
			return false;
		} else {
			Node n = goal;
			Node target;
			try {
				target = n.getParent().getParent();
			} catch (NullPointerException e) {
				target = null;
			}
			while(target != null) {
				nea[n.getX()][n.getY()]++;
				if(LineOfSight.isVisible_edge_zeroWidth(n,target,map,false)) {
					n.setParent(target);
					target = target.getParent();
					
				} else {
					n = n.getParent();
					target = n.getParent().getParent();
				}
			}
			return true;
		}
	}
	
}



