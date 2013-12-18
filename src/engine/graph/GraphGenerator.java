package engine.graph;

import utility.Coordinate;
import engine.map.Map;

public class GraphGenerator {
	
	/*
	 * Could both of these could be sped up with dynamic programming? You would only set neighbours below, to the bottom right and
	 * to the right of you, but you would have to set yourself as their neighbour too. Not sure this necessarily saves much time
	 * because the total number of sets is the same, but it may sounds good in my diss!
	 */

	/* This version of generateGraph adds any neighbour that isn't blocked.
	 * For simple A* this is fine IF you assume the agent has zero width. However
	 * if the agent does have non-zero width then it will be incorrect as this way allows
	 * agents to say go from a cell to its north-east cell even if the east (AND/or north)
	 * cell is blocked. However, if this is allowed then it should be noted that the 
	 * efficient version of Bresenham is suitable.
	 */
	public static Graph generateGraph_centre_zeroWidth(Map map){ 

		Node[][] graphArray2D = new Node[map.getWidth()][map.getHeight()];
		for(int j=0; j < map.getHeight(); j++) {
			for(int i=0; i < map.getWidth(); i++) {
				if(map.getCell(i,j).isBlocked()) {
					graphArray2D[i][j] = null;
				} else {
					/*
					 * is the next line efficient? I know the Coordinate I want to pass to Node as a param 
					 * so could make it directly, but then i'd have duplicate Coordinates with same x and y 
					 * in memory, hence why I did what I did
					 */
					graphArray2D[i][j] = new Node(map.getCell(i, j).getCoordinate());
				}
			}
		}

		//should put these (also used in MapGenerator) in some kind of Data class
		Coordinate[] adjacentRelativeCoordinates = {new Coordinate(0,-1), new Coordinate(-1,0), new Coordinate(1,0), new Coordinate(0,1), new Coordinate(-1,-1), new Coordinate(-1,1), new Coordinate(1,-1), new Coordinate(1,1)};
		for(int j=0; j < map.getHeight(); j++) {
			for(int i=0; i < map.getWidth(); i++) {

				if(graphArray2D[i][j] != null) {

					for(int k=0; k < adjacentRelativeCoordinates.length; k++) {
						try {
							Node neighbour = graphArray2D[i + adjacentRelativeCoordinates[k].getX()][j + adjacentRelativeCoordinates[k].getY()];
							if(neighbour != null) {
								graphArray2D[i][j].addNeighbour(neighbour);
								//System.out.println(neighbour.coordinateAsString() + " added to " + graphArray2D[i][j].coordinateAsString());
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							//skip if adjacent cell to be adjusted is already blocked
						}
					}
				}
			}
		}

		Graph graph = new Graph();
		for(int j=0; j<graphArray2D[0].length; j++) {
			for(int i=0; i<graphArray2D.length; i++) {
				if(graphArray2D[i][j] != null) {
					graph.addNode(graphArray2D[i][j]);
				}
			}
		}
		graph.setSource(graphArray2D[0][0]);
		graph.setSource(graphArray2D[graphArray2D.length-1][graphArray2D[0].length-1]);
		return graph;

	}

	/* note, this only differs from generateGraph in the k loop
	 * now, I have 2 separate arrays of adjacent coordinates:
	 * the ones that are above/left/right/below: adjacentRelativeCoordinates
	 * and the ones that are diagonal: diagonalRelativeCoordinates
	 * if the cell to the left of the current cell is blocked, then we make
	 * sure that left, upperleft and lowerleft are not neighbours by removing
	 * upperleft and lowerleft from the diagonalRelativeCoordinates array
	 */
	public static Graph generateGraph_centre_finiteWidth(Map map){ 

        Node[][] graphArray2D = new Node[map.getWidth()][map.getHeight()];
        for(int j=0; j < map.getHeight(); j++) {
                for(int i=0; i < map.getWidth(); i++) {
                        Coordinate coordinate = map.getCell(i, j).getCoordinate(); //i assume this sort of thing gets optimized out by the compiler (i.e. remaking a coordinate object every iteration)
                        if(map.getCell(coordinate).isBlocked()) {
                                graphArray2D[i][j] = null;
                        } else {
                                /*
                                 * is the next line efficient? I know the Coordinate I want to pass to Node as a param 
                                 * so could make it directly, but then i'd have duplicate Coordinates with same x and y 
                                 * in memory, hence why I did what I did
                                 */
                                graphArray2D[i][j] = new Node(map.getCell(i, j).getCoordinate());
                        }
                }
        }

        //should put these (also used in MapGenerator) in some kind of Data class
        Coordinate[] adjacentRelativeCoordinates = {new Coordinate(0,1), new Coordinate(1,0), new Coordinate(0,-1), new Coordinate(-1,0)};
        for(int j=0; j < map.getHeight(); j++) {
                for(int i=0; i < map.getWidth(); i++) {

                        if(graphArray2D[i][j] != null) {
                                Coordinate[] diagonalRelativeCoordinates = {new Coordinate(-1,1), new Coordinate(1,1), new Coordinate(1,-1), new Coordinate(-1,-1)};
                                for(int k=0; k < adjacentRelativeCoordinates.length; k++) {
                                        try {
                                                Node neighbour = graphArray2D[i + adjacentRelativeCoordinates[k].getX()][j + adjacentRelativeCoordinates[k].getY()];
                                                if(neighbour != null) {
                                                        graphArray2D[i][j].addNeighbour(neighbour);
                                                        //System.out.println(neighbour.coordinateAsString() + " added to " + graphArray2D[i][j].coordinateAsString());
                                                } else {
                                                        diagonalRelativeCoordinates[k] = null;
                                                        diagonalRelativeCoordinates[((k+1)%adjacentRelativeCoordinates.length)] = null;
                                                }
                                        } catch (ArrayIndexOutOfBoundsException e) {
                                                //skip if adjacent cell to be adjusted is already blocked
                                        }
                                }
                                for(int k=0; k < diagonalRelativeCoordinates.length; k++) {
                                        try {
                                                if(diagonalRelativeCoordinates[k] != null) {
                                                        Node neighbour = graphArray2D[i + diagonalRelativeCoordinates[k].getX()][j + diagonalRelativeCoordinates[k].getY()];
                                                        if(neighbour != null) {
                                                                graphArray2D[i][j].addNeighbour(neighbour);
                                                                //System.out.println(neighbour.coordinateAsString() + " added to " + graphArray2D[i][j].coordinateAsString());
                                                        }
                                                }
                                        } catch (ArrayIndexOutOfBoundsException e) {
                                                //skip if adjacent cell to be adjusted is already blocked
                                        }
                                }
                        }
                }
        }

        Graph graph = new Graph();
        for(int j=0; j<graphArray2D[0].length; j++) {
                for(int i=0; i<graphArray2D.length; i++) {
                        if(graphArray2D[i][j] != null) {
                                graph.addNode(graphArray2D[i][j]);
                        }
                }
        }
        graph.setSource(graphArray2D[0][0]);
		graph.setSource(graphArray2D[graphArray2D.length-1][graphArray2D[0].length-1]);
        return graph;
	}
	
	public static Graph generateGraph_edge_zeroWidth(Map map){ 
		Coordinate[] diagonalRelativeCellCoordinates = {new Coordinate(-1,-1), new Coordinate(0,-1), new Coordinate(0,0), new Coordinate(-1,0)};
		Coordinate[] diagonalRelativeNodeCoordinates = {new Coordinate(-1,-1), new Coordinate(1,-1), new Coordinate(1,1), new Coordinate(-1,1)};
		Coordinate[] adjacentRelativeNodeCoordinates = {new Coordinate(0,-1), new Coordinate(1,0), new Coordinate(0,1), new Coordinate(-1,0)};
		Node[][] graphArray2D = new Node[map.getWidth()+1][map.getHeight()+1];
		for(int j=0; j < map.getHeight()+1; j++) {
			for(int i=0; i < map.getWidth()+1; i++) {
				boolean isUnblockedAdjacentCell = false;
				for(Coordinate c: diagonalRelativeCellCoordinates) {
					try {
						if(!map.getCell(i+c.getX(),j+c.getY()).isBlocked()) {
							isUnblockedAdjacentCell=true;
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
				}
				if(!isUnblockedAdjacentCell) {
					graphArray2D[i][j] = null;
				} else {
					graphArray2D[i][j] = new Node(new Coordinate(i,j));
				}
			}
		}
		//separately add Source and sink
		graphArray2D[0][0] = new Node(new Coordinate(0,0));
		graphArray2D[graphArray2D.length-1][graphArray2D[0].length-1] = new Node(new Coordinate(graphArray2D.length-1,graphArray2D[0].length-1));
				
		for(int j=0; j < map.getHeight()+1; j++) {
			for(int i=0; i < map.getWidth()+1; i++) {
				if(graphArray2D[i][j] != null) {
					//adds in diagonally adjacent nodes
					for(int k=0;k<4;k++) {
						try {
							Node neighbour = graphArray2D[i+diagonalRelativeNodeCoordinates[k].getX()][j+diagonalRelativeNodeCoordinates[k].getY()];
							if(!map.getCell(i+diagonalRelativeCellCoordinates[k].getX(),j+diagonalRelativeCellCoordinates[k].getY()).isBlocked() && neighbour!=null) {
								graphArray2D[i][j].addNeighbour(neighbour);
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							//do nothing
						}
					}
					//adds in horizontally/vertically adjacent nodes
					for(int k=0;k<4;k++) {
						boolean okForAdjacent = false;
						try {
							if(!map.getCell(i+diagonalRelativeCellCoordinates[k].getX(),j+diagonalRelativeCellCoordinates[k].getY()).isBlocked()) {
								okForAdjacent = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						try{
							if(!map.getCell(i+diagonalRelativeCellCoordinates[(k+1)%4].getX(),j+diagonalRelativeCellCoordinates[(k+1)%4].getY()).isBlocked()) {		
								okForAdjacent = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						if(okForAdjacent) {
							Node neighbour = graphArray2D[i+adjacentRelativeNodeCoordinates[k].getX()][j+adjacentRelativeNodeCoordinates[k].getY()];
							if(neighbour!=null) {
								graphArray2D[i][j].addNeighbour(neighbour);
							}
						}
					}
				}
			}
		}

		Graph graph = new Graph();
		for(int j=0; j<graphArray2D[0].length; j++) {
			for(int i=0; i<graphArray2D.length; i++) {
				if(graphArray2D[i][j] != null) {
					graph.addNode(graphArray2D[i][j]);
				}
			}
		}
		graph.setSource(graphArray2D[0][0]);
		graph.setGoal(graphArray2D[graphArray2D.length-1][graphArray2D[0].length-1]);
		return graph;

	}
	
	public static Graph generateGraph_edge_finiteWidth(Map map){ 
		Coordinate[] diagonalRelativeCellCoordinates = {new Coordinate(-1,-1), new Coordinate(0,-1), new Coordinate(0,0), new Coordinate(-1,0)};
		Coordinate[] diagonalRelativeNodeCoordinates = {new Coordinate(-1,-1), new Coordinate(1,-1), new Coordinate(1,1), new Coordinate(-1,1)};
		Coordinate[] adjacentRelativeNodeCoordinates = {new Coordinate(0,-1), new Coordinate(1,0), new Coordinate(0,1), new Coordinate(-1,0)};
		Node[][] graphArray2D = new Node[map.getWidth()+1][map.getHeight()+1];
		for(int j=0; j < map.getHeight()+1; j++) {
			for(int i=0; i < map.getWidth()+1; i++) {
				boolean diag1ok = false;
				boolean diag2ok = false;
				for(int k=0;k<4;k++) {
					try {
						if(!map.getCell(i+diagonalRelativeCellCoordinates[k].getX(),j+diagonalRelativeCellCoordinates[k].getY()).isBlocked()) {
							if(k%2==0) {
								diag1ok = true;
							} else {
								diag2ok = true;
							}
						}
					} catch(ArrayIndexOutOfBoundsException e) {}
				}
				if(diag1ok && diag2ok) {
					graphArray2D[i][j] = new Node(new Coordinate(i,j));
				} else {
					graphArray2D[i][j] = null;
				}
			}
		}
		//separately add Source and sink
		graphArray2D[0][0] = new Node(new Coordinate(0,0));
		graphArray2D[graphArray2D.length-1][graphArray2D[0].length-1] = new Node(new Coordinate(graphArray2D.length-1,graphArray2D[0].length-1));
		
		for(int j=0; j < map.getHeight()+1; j++) {
			for(int i=0; i < map.getWidth()+1; i++) {
				if(graphArray2D[i][j] != null) {
					//adds in diagonally adjacent nodes
					for(int k=0;k<4;k++) {
						try {
							Node neighbour = graphArray2D[i+diagonalRelativeNodeCoordinates[k].getX()][j+diagonalRelativeNodeCoordinates[k].getY()];
							if(!map.getCell(i+diagonalRelativeCellCoordinates[k].getX(),j+diagonalRelativeCellCoordinates[k].getY()).isBlocked() && neighbour!=null) {
								graphArray2D[i][j].addNeighbour(neighbour);
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							//do nothing
						}
					}
					//adds in horizontally/vertically adjacent nodes
					for(int k=0;k<4;k++) {
						boolean okForAdjacent = false;
						try {
							if(!map.getCell(i+diagonalRelativeCellCoordinates[k].getX(),j+diagonalRelativeCellCoordinates[k].getY()).isBlocked()) {
								okForAdjacent = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						try{
							if(!map.getCell(i+diagonalRelativeCellCoordinates[(k+1)%4].getX(),j+diagonalRelativeCellCoordinates[(k+1)%4].getY()).isBlocked()) {		
								okForAdjacent = true;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						if(okForAdjacent) {
							Node neighbour = graphArray2D[i+adjacentRelativeNodeCoordinates[k].getX()][j+adjacentRelativeNodeCoordinates[k].getY()];
							if(neighbour!=null) {
								graphArray2D[i][j].addNeighbour(neighbour);
							}
						}
					}
				}
			}
		}

		Graph graph = new Graph();
		for(int j=0; j<graphArray2D[0].length; j++) {
			for(int i=0; i<graphArray2D.length; i++) {
				if(graphArray2D[i][j] != null) {
					graph.addNode(graphArray2D[i][j]);
				}
			}
		}
		graph.setSource(graphArray2D[0][0]);
		graph.setGoal(graphArray2D[graphArray2D.length-1][graphArray2D[0].length-1]);
		return graph;

	}
	
	//NB TO HAVE VIS GRAPH WITH ZERO EDGES I WOULD NEED TO SPECIFICALLY ADD NODES WHEN YOU HAVE 2 BLOCKED CELLS ON A DIAGONAL
	public static Graph generateGraph_visibility_edge_finiteWidth(Map map){ 
		Coordinate[] diagonalRelativeCellCoordinates = {new Coordinate(-1,-1), new Coordinate(0,-1), new Coordinate(0,0), new Coordinate(-1,0)};
		Node[][] graphArray2D = new Node[map.getWidth()+1][map.getHeight()+1];
		for(int j=0; j < map.getHeight()+1; j++) {
			for(int i=0; i < map.getWidth()+1; i++) {
				//nodes don't exist on boundaries except start and end
				int cellsBlocked = 0;;
				try{
					for(int k=0;k<4;k++) {
						if(map.getCell(i+diagonalRelativeCellCoordinates[k].getX(),j+diagonalRelativeCellCoordinates[k].getY()).isBlocked()) {
							cellsBlocked++;
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {cellsBlocked = 10;}
				if(cellsBlocked == 1) {
					graphArray2D[i][j] = new Node(new Coordinate(i,j));
				}
			}
		}
		//separately add Source and sink
		graphArray2D[0][0] = new Node(new Coordinate(0,0));
		graphArray2D[graphArray2D.length-1][graphArray2D[0].length-1] = new Node(new Coordinate(graphArray2D.length-1,graphArray2D[0].length-1));
		for(int j=0; j < map.getHeight()+1; j++) {
			for(int i=0; i < map.getWidth()+1; i++) {
				for(int l=0; l < map.getHeight()+1; l++) {
					for(int k=0; k < map.getWidth()+1; k++) {
						if(!(i==k && j==l)) {
							if(graphArray2D[i][j]!=null && graphArray2D[k][l]!=null && LineOfSight.isVisible_edge_finiteWidth(graphArray2D[i][j], graphArray2D[k][l], map)) {
								graphArray2D[i][j].addNeighbour(graphArray2D[k][l]);
							}
						}
					}
				}
			}
		}
		Graph graph = new Graph();
		for(int j=0; j<graphArray2D[0].length; j++) {
			for(int i=0; i<graphArray2D.length; i++) {
				if(graphArray2D[i][j] != null) {
					graph.addNode(graphArray2D[i][j]);
				}
			}
		}
		graph.setSource(graphArray2D[0][0]);
		graph.setGoal(graphArray2D[graphArray2D.length-1][graphArray2D[0].length-1]);
		return graph;

	}
	

}

