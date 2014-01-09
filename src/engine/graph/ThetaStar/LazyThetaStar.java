package engine.graph.ThetaStar;

import java.util.List;

import engine.graph.LineOfSight;
import engine.graph.Node;
import engine.graph.AStar.AStar;
import engine.map.Map;

public class LazyThetaStar extends AStar {

	private static final long serialVersionUID = 1L;

	public LazyThetaStar() {
		super();
	}
	
	@Override
	protected boolean updateCost(Node current, Node neighbour, Node goal, Map map) {
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
				neighbour.setG(prosposedNewGScore);
				neighbour.setF(prosposedNewGScore + getDistance(neighbour,goal));
				return true;
			} else {
				return false;
			}
		}
	}
	
	@Override
	protected void setNode(Node current, List<Node> closedSet, Map map) {
		if(current.getParent()!=null) {
			if(!LineOfSight.isVisible_edge_zeroWidth(current.getParent(), current, map)) {
				Node bestNeighbour = null;
				double lowestScore = Double.POSITIVE_INFINITY;
				for(Node n : current.getNeighbours()) {
					double score = n.getG()+getDistance(current,n);
					if(score < lowestScore && closedSet.contains(n)) {
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

}
