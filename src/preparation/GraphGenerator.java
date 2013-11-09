package preparation;

public class GraphGenerator {

	/* This version of generateGraph adds any neighbour that isn't blocked.
	 * For simple A* this is fine IF you assume the agent has zero width. However
	 * if the agent does have non-zero width then it will be incorrect as this way allows
	 * agents to say go from a cell to its north-east cell even if the east (AND/or north)
	 * cell is blocked. However, if this is allowed then it should be noted that the 
	 * efficient version of Bresenham is suitable.
	 */
	public static Graph generateGraph(Map map){ 

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
	public static Graph generateGraph_v2(Map map){ 

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
		return graph;

	}

}

