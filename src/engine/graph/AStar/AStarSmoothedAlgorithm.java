package engine.graph.AStar;

import engine.graph.LineOfSight;
import engine.graph.Node;
import engine.map.Map;

public class AStarSmoothedAlgorithm {
	
	/*
	 * simple method that implements smoothing.
	 * Starts at goal node and do line of sight on parent of parent: 
	 * if in LoS we reparent to the parent of its parent and go again
	 * else we start LoS tests from parent
	 */
	public static void smoothe_centre(Node goal) { //i.e. start of path and goal of path, but we go in reverse so we start at the goal!
		Node current = goal;
		Node target = current.getParent().getParent();
		while(target != null) {
			if(LineOfSight.isVisible_centre_finiteWidth(current, target)) {
				current.setParent(target);
				target = target.getParent();
				
			} else {
				current = current.getParent();
				target = current.getParent().getParent();
			}
		}
	}
	
	public static void smoothe_edge(Node goal, Map map) { //i.e. start of path and goal of path, but we go in reverse so we start at the goal!
		Node current = goal;
		Node target;
		try {
			target = current.getParent().getParent();
		} catch (NullPointerException e) {
			target = null;
		}
		while(target != null) {
			if(LineOfSight.isVisible_edge_finiteWidth(current,target,map)) {
				current.setParent(target);
				target = target.getParent();
				
			} else {
				current = current.getParent();
				target = current.getParent().getParent();
			}
		}
	}

}
