package engine.graph;

public class AStarSmoothed {
	
	public static void smoothe(Node start, Node goal) { //i.e. start of path and goal of path, but we go in reverse so we start at the goal!
		Node current = goal;
		Node target = current.getParent().getParent();
		while(target != null) {
			if(Bresenham.isVisible(current, target)) {
				current.setParent(target);
				target = target.getParent();
				
			} else {
				current = current.getParent();
				target = current.getParent().getParent();
			}
		}
	}

}
