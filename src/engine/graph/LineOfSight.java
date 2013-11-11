package engine.graph;

public class LineOfSight {

	private static Node from;
	private static Node to;
	private static Node current;
	private static Node temp;

	private static int x0;
	private static int x1;
	private static int y0;
	private static int y1;
	private static int x;
	private static int y;

	
	/*
	 * implements a line of sight check based on Bresenham's line drawing algorithm
	 * Instead of using the pure line-drawing algorithm as advised in the Theta* web implementation
	 * I have made sure that each node on the line of sight is reachable (i.e. a neighbour) of the 
	 * node before it in the line of sight. This ensures that the line of sight check produces
	 * straight paths that are walkable for the character (i.e. not slipping between and around blocks
	 * on paths that aren't possible according to the graph)
	 */
	public static boolean isVisible(Node fromArg, Node toArg) {
		if(fromArg == null || toArg == null) {
			return false;
		} else {
			from = fromArg;
			to = toArg;
			current = from;
			double m = ((double)(to.getY()-from.getY()))/((double)(to.getX()-from.getX()));

			if(m <= 1 && m >= -1) { //octant 1,8,4 or 5
				//if octant 4 or 5, swap from and to
				if(from.getX() > to.getX()) {
					temp = from; from = to; to = temp; //tried to do with separate method but the temp screwed it up, dunno if something to do with static vars
					current = from; //??
				}
				initialiseVariables();
				//DO I NEED TO CHECK THAT THE FROM NODE IS BLOCKED??

				double yActual = y0;
				while(x < x1) {
					x++;
					yActual += m;
					y = (int) Math.round(yActual);
					if((temp = current.getNeighbourIfExists(x,y)) != null) {	//aka: //temp = current.getReachable(x, y); if(temp != null) {
						current = temp;
					} else {
						return false;
					}
				} 
			} else {			//octant 2,3,6 or 7
				//if octant 6 or 7, swap from and to
				if(from.getY() > to.getY()) {
					temp = from; from = to; to = temp; 
					current = from; //??
				}
				initialiseVariables();
				double xActual = x0;
				m = 1/m;
				while(y < y1) {
					y++;
					xActual += m;
					x = (int) Math.round(xActual);
					if((temp = current.getNeighbourIfExists(x,y)) != null) {						//aka temp = current.getReachable(x, y); if(temp != null) {
						current = temp;
					} else {
						return false;
					}
				}
			}
			return true;
		}
	}

	private static void initialiseVariables() {
		x0 = from.getX();
		x1 = to.getX();
		y0 = from.getY();
		y1 = to.getY();
		x = x0;
		y = y0;
	}

}
