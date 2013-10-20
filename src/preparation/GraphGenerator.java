package preparation;

public class GraphGenerator {

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
								graphArray2D[i][j].addSuccessor(neighbour);
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

}

