package engine.graph;

import engine.map.Map;

public class LineOfSight {
	
	public static boolean isVisible_edge_zeroWidth(Node from, Node to, Map map, boolean centre) {
		if(from == null || to == null) {
			return false;
		}
		if(from.getCoordinate().equals(to.getCoordinate())) {
			return true;
		}
		int x0 = from.getX();
		int y0 = from.getY();
		int x1 = to.getX();
		int y1 = to.getY();
		
		int dy = y1-y0;
		int dx = x1-x0;
		
		int f = 0;
		
		//sx and sy are the same as the paper - sxC and syC are in place of the (sx-1)/2 stuff
		int sx, sxC, sy, syC;
		if(dy<0) {
			dy = -1*dy;
			sy = -1; 
			if(!centre) { syC = -1; } else { syC = 0; }
		} else {
			sy = 1; syC = 0;
		}
		if(dx<0) {
			dx = -1*dx;
			sx = -1;
			if(!centre) { sxC = -1; } else { sxC = 0; }
		} else {
			sx = 1; sxC = 0;
		}
		
		if(dx >= dy) {
			while(x0 != x1) {
				f+=dy;
				if(f>=dx) {
					if(map.getCell(x0+sxC, y0+syC).isBlocked()) {
						return false;
					}
					y0+=sy;
					f-=dx;
				}
				if(f!=0 && map.getCell(x0+sxC, y0+syC).isBlocked()) {
					return false;
				}
				if(dy==0) {
					boolean blocked1 = false;
					boolean blocked2 = false;
					try {
						blocked1 = map.getCell(x0+sxC, y0).isBlocked();
					} catch (ArrayIndexOutOfBoundsException e) {
						blocked1 = true;
					}
					try{
						blocked2 = map.getCell(x0+sxC, y0-1).isBlocked();
					} catch (ArrayIndexOutOfBoundsException e) {
						blocked2= true;
					}
					if(blocked1 && blocked2) {
						return false;
					}
				}
				x0+=sx;
			}
		} else {
			while(y0 != y1) {
				f+=dx;
				if(f>=dy) {
					if(map.getCell(x0+sxC, y0+syC).isBlocked()) {
						return false;
					}
					x0+=sx;
					f-=dy;
				}
				if(f!=0 && map.getCell(x0+sxC, y0+syC).isBlocked()) {
					return false;
				}
				if(dx==0) {
					boolean blocked1 = false;
					boolean blocked2 = false;
					try {
						blocked1 = map.getCell(x0, y0+syC).isBlocked();
					} catch (ArrayIndexOutOfBoundsException e) {
						blocked1 = true;
					}
					try {
						blocked2 = map.getCell(x0-1, y0+syC).isBlocked();
					} catch (ArrayIndexOutOfBoundsException e) {
						blocked2 = true;
					}
					if(blocked1 && blocked2) {
						return false;
					}
				}
				y0+=sy;
			}
		}
		return true;
	}
	
	public static boolean isVisible_edge_finiteWidth(Node from, Node to, Map map) { return false; }
	
	public static boolean isVisible_centre_finiteWidth(Node from, Node to) { return false; }
	
	/*
	 * implements a line of sight check based on Bresenham's line drawing algorithm
	 * Instead of using the pure line-drawing algorithm as advised in the Theta* web implementation
	 * I have made sure that each node on the line of sight is reachable (i.e. a neighbour) of the 
	 * node before it in the line of sight. This ensures that the line of sight check produces
	 * straight paths that are walkable for the character (i.e. not slipping between and around blocks
	 * on paths that aren't possible according to the graph)
	 */
	
	/*private static Node from;
	private static Node to;
	private static Node current;
	private static Node temp;

	private static int x0;
	private static int x1;
	private static int y0;
	private static int y1;
	private static int x;
	private static int y;
	
	public static boolean isVisible_centre_finiteWidth(Node fromArg, Node toArg) {
		if(fromArg == null || toArg == null) {
			return false;
		} else {
			from = fromArg;
			to = toArg;
			current = from;
			double m = ((double)(to.getY()-from.getY()))/((double)(to.getX()-from.getX()));		//double division doesn't throw an exception when dividing by zero but gives

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
				if(m == Double.POSITIVE_INFINITY || m == Double.NEGATIVE_INFINITY) {
					m = 0;
				} else {
					m = 1/m;
				}					//not dangerous because we know m!=0 as this would have been in the if statement
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

	public static boolean isVisible_edge_zeroWidth(Node fromArg, Node toArg, Map map) {
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
				double yActual = y0;
				while(x<x1) {
					x++;
					yActual+=m;
					int yActualInt = (int) fp(yActual);
					if(m==0.0) {
						boolean oneAboveOrBelowIsFree = false;
						try {
							if(!map.getCell(x-1,yActualInt).isBlocked()){
								oneAboveOrBelowIsFree = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						try {
							if(!map.getCell(x-1,yActualInt-1).isBlocked()){
								oneAboveOrBelowIsFree = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						if(!oneAboveOrBelowIsFree) {
							return false;
						}
					} else if (m>0) {
						if(Math.abs(yActual - (int) Math.round(yActual)) <= 0.001) {
							//yActualInt =(int) Math.round(yActual);
							if(map.getCell(x-1,yActualInt-1).isBlocked()) {// if(map.getCell(x-1,(int)(yActual-0.5)).isBlocked()) {
								return false;
							}
						} else { 
							if((int) (yActual-m) == yActualInt) {
								if(map.getCell(x-1,yActualInt).isBlocked()) {
									return false;
								}
							} else {
								if(map.getCell(x-1,yActualInt).isBlocked() || map.getCell(x-1,yActualInt-1).isBlocked())  {
									return false;
								}
							}
						}
					} else {
						if(Math.abs(yActual - (int) Math.round(yActual)) <= 0.001) {
							//yActualInt = (int) Math.round(yActual);
							if(map.getCell(x-1,yActualInt).isBlocked()) {//if(map.getCell(x-1,(int)(yActual+0.5)).isBlocked()) {
								return false;
							}
						} else {
							if((int) Math.ceil(yActual-m) == yActualInt+1) {		//if((int) (yActual-m) == yActualInt) {
								if(map.getCell(x-1,yActualInt).isBlocked()) {
									return false;
								}
							} else {
								if(map.getCell(x-1,yActualInt).isBlocked() || map.getCell(x-1,yActualInt+1).isBlocked())  {
									return false;
								}
							}
						}
					}
				}
				return true;
			} else {			//octant 2,3,6 or 7
				//if octant 6 or 7, swap from and to
				if(from.getY() > to.getY()) {
					temp = from; from = to; to = temp; 
					current = from; //??
				}
				initialiseVariables();
				double xActual = x0;
				if(x0 == x1) {
					m = 0.0;
				} else {
					m = 1/m;
				}
				while(y < y1) {
					y++;
					xActual += m;
					int xActualInt = (int) fp(xActual);
					if(m==0.0) {
						boolean oneAboveOrBelowIsFree = false;
						try {
							if(!map.getCell(xActualInt, y-1).isBlocked()){
								oneAboveOrBelowIsFree = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						try {
							if(!map.getCell(xActualInt-1,y-1).isBlocked()){
								oneAboveOrBelowIsFree = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						if(!oneAboveOrBelowIsFree) {
							return false;
						}
					} else if (m>0) {	//i.e oct2
						if(Math.abs(xActual - xActualInt) <= 0.001) {
							//xActualInt = (int) Math.round(xActual);
							if(map.getCell(xActualInt-1,y-1).isBlocked()) {
								return false;
							}
						} else {
							if((int) (xActual-m) == xActualInt) {
								if(map.getCell(xActualInt,y-1).isBlocked()) {
									return false;
								}
							} else {
								if(map.getCell(xActualInt,y-1).isBlocked() || map.getCell(xActualInt-1,y-1).isBlocked())  {
									return false;
								}
							}
						}
					} else {	//oct 3
						if(Math.abs(xActual - xActualInt) <= 0.001) {
							//xActualInt = (int) Math.round(xActual);
							if(map.getCell(xActualInt,y-1).isBlocked()) {
								return false;
							}
						} else {
							if((int) Math.ceil((xActual-m))== xActualInt+1) {
								if(map.getCell(xActualInt,y-1).isBlocked()) {
									return false;
								}
							} else {
								if(map.getCell(xActualInt,y-1).isBlocked() || map.getCell(xActualInt+1,y-1).isBlocked())  {
									return false;
								}
							}
						}
					}
				}
				return true;
			}
		}
	}

	public static boolean isVisible_edge_finiteWidth(Node fromArg, Node toArg, Map map) {
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
				double yActual = y0;
				while(x<x1) {
					x++;
					yActual+=m;
					int yActualInt = (int) fp(yActual);
					if(m==0.0) {
						// Cells are 	1 3	 2 nodes in question are at bottom right of 1 and bottom right of 3
						//				2 4
						boolean oneAboveOrBelowIsFree = false;
						boolean cell1Blocked, cell2Blocked, cell3Blocked, cell4Blocked;
						cell1Blocked = cell2Blocked = cell3Blocked = cell4Blocked = false;
						//cell 1
						try {
							if(map.getCell(x-1,yActualInt).isBlocked()){
								cell1Blocked = true;
							} else {
								oneAboveOrBelowIsFree = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						//cell2
						try {
							if(map.getCell(x-1,yActualInt-1).isBlocked()){
								cell2Blocked = true;
							} else {
								oneAboveOrBelowIsFree = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						//cell3
						try {
							if(map.getCell(x,yActualInt-1).isBlocked()){
								cell3Blocked = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						//cell4
						try {
							if(map.getCell(x,yActualInt).isBlocked()){
								cell4Blocked = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						if(!oneAboveOrBelowIsFree || (cell1Blocked && cell3Blocked) || (cell2Blocked && cell4Blocked)) {
							return false;
						}
					} else if (m>0) {
						if(Math.abs(yActual - (int) Math.round(yActual)) <= 0.001) {
							//yActualInt =(int) Math.round(yActual);
							if(map.getCell(x-1,yActualInt-1).isBlocked()) {// if(map.getCell(x-1,(int)(yActual-0.5)).isBlocked()) {
								return false;
							}
							try {
								if(map.getCell(x-1,yActualInt).isBlocked() && map.getCell(x,yActualInt-1).isBlocked()) {
									return false;
								}
							} catch (ArrayIndexOutOfBoundsException e) {}
						} else { 
							if((int) (yActual-m) == yActualInt) {
								if(map.getCell(x-1,yActualInt).isBlocked()) {
									return false;
								}
							} else {
								if(map.getCell(x-1,yActualInt).isBlocked() || map.getCell(x-1,yActualInt-1).isBlocked())  {
									return false;
								}
							}
						}
					} else {
						if(Math.abs(yActual - (int) Math.round(yActual)) <= 0.001) {
							//yActualInt = (int) Math.round(yActual);
							if(map.getCell(x-1,yActualInt).isBlocked()) {//if(map.getCell(x-1,(int)(yActual+0.5)).isBlocked()) {
								return false;
							}
							try {
								if(map.getCell(x-1,yActualInt-1).isBlocked() && map.getCell(x,yActualInt).isBlocked()) {
									return false;
								}
							} catch (ArrayIndexOutOfBoundsException e) {}
						} else {
							if((int) Math.ceil(yActual-m) == yActualInt+1) {		//if((int) (yActual-m) == yActualInt) {
								if(map.getCell(x-1,yActualInt).isBlocked()) {
									return false;
								}
							} else {
								if(map.getCell(x-1,yActualInt).isBlocked() || map.getCell(x-1,yActualInt+1).isBlocked())  {
									return false;
								}
							}
						}
					}
				}
				return true;
			} else {			//octant 2,3,6 or 7
				//if octant 6 or 7, swap from and to
				if(from.getY() > to.getY()) {
					temp = from; from = to; to = temp; 
					current = from; //??
				}
				initialiseVariables();
				double xActual = x0;
				if(x0 == x1) {
					m = 0.0;
				} else {
					m = 1/m;
				}
				while(y < y1) {
					y++;
					xActual += m;
					int xActualInt = (int) fp(xActual);
					if(m==0.0) {
						boolean oneAboveOrBelowIsFree = false;
						boolean cell1Blocked, cell2Blocked, cell3Blocked, cell4Blocked;
						cell1Blocked = cell2Blocked = cell3Blocked = cell4Blocked = false;
						//cell 1
						try {
							if(map.getCell(xActualInt,y-1).isBlocked()){
								cell1Blocked = true;
							} else {
								oneAboveOrBelowIsFree = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						//cell2
						try {
							if(map.getCell(xActualInt-1,y-1).isBlocked()){
								cell2Blocked = true;
							} else {
								oneAboveOrBelowIsFree = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						//cell3
						try {
							if(map.getCell(xActualInt-1,y).isBlocked()){
								cell3Blocked = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						//cell4
						try {
							if(map.getCell(xActualInt,y).isBlocked()){
								cell4Blocked = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						if(!oneAboveOrBelowIsFree || (cell1Blocked && cell3Blocked) || (cell2Blocked && cell4Blocked)) {
							return false;
						}
					} else if (m>0) {	//i.e oct2
						if(Math.abs(xActual - xActualInt) <= 0.001) {
							//xActualInt = (int) Math.round(xActual);
							if(map.getCell(xActualInt-1,y-1).isBlocked()) {
								return false;
							}
							try {
								if(map.getCell(xActualInt,y-1).isBlocked() && map.getCell(xActualInt-1,y).isBlocked()) {
									return false;
								}
							} catch (ArrayIndexOutOfBoundsException e) {}
						} else {
							if((int) (xActual-m) == xActualInt) {
								if(map.getCell(xActualInt,y-1).isBlocked()) {
									return false;
								}
							} else {
								if(map.getCell(xActualInt,y-1).isBlocked() || map.getCell(xActualInt-1,y-1).isBlocked())  {
									return false;
								}
							}
						}
					} else {	//oct 3
						if(Math.abs(xActual - xActualInt) <= 0.001) {
							//xActualInt = (int) Math.round(xActual);
							if(map.getCell(xActualInt,y-1).isBlocked()) {
								return false;
							}
							try {
								if(map.getCell(xActualInt-1,y-1).isBlocked() && map.getCell(xActualInt,y).isBlocked()) {
									return false;
								}
							} catch (ArrayIndexOutOfBoundsException e) {}
						} else {
							if((int) Math.ceil((xActual-m))== xActualInt+1) {
								if(map.getCell(xActualInt,y-1).isBlocked()) {
									return false;
								}
							} else {
								if(map.getCell(xActualInt,y-1).isBlocked() || map.getCell(xActualInt+1,y-1).isBlocked())  {
									return false;
								}
							}
						}
					}
				}
				return true;
			}
		}
	}

	
	//this function allows for rounding errors
	private static double fp(double d) {
		if(Math.abs(Math.round(d) - d) <= 0.001) {
			return Math.round(d);
		} else {
			return d;
		}
	}


	private static void initialiseVariables() {
		x0 = from.getX();
		x1 = to.getX();
		y0 = from.getY();
		y1 = to.getY();
		x = x0;
		y = y0;
	}*/

}
