package preparation;

public class AStarSmoothing {
	
	public static void smoothe(Node start, Node goal) { //i.e. start of path and goal of path, but we go in reverse so we start at the goal!
		Node current = goal;
		Node target = current.getParent().getParent();
		while(target != start) {
			System.out.print("Current at: " + current.getX() + "," + current.getY());
			System.out.print(". Target at: " + target.getX() + "," + target.getY());
			System.out.print("\n");
			if(Bresenham_v2.isVisible(current, target)) {
				current.setParent(target);
				System.out.print(". Current re-parented to " + target.getX() + "," + target.getY());
				target = target.getParent();
				
			} else {
				current = current.getParent();
				target = current.getParent().getParent();
				System.out.print(". No LoV");
			}
			System.out.print("\n");
		}
	}

}
