package preparation;

/*
 * can make this much more efficient by only ever setting neighbours to left, bottom left and below
 * as long as you also set them to point back to you at the same time (kinda taking inspiration from
 * dynamic programming), as long as you sweep left to right, then down a column, then left to right etc
 */

//this class is in some dire need of some design patterns (or is it?)

//should I be using Location objects (or subclasses thereof to deal with potential fields)? Should I export the map as Location[][]? Probably

public class MapGenerator {

	private static int[][] map;					//is this suitably efficient?			
	private static int totalPotential = 0; 		//this is a sum of the potential scores on the entire map (initialised with 1 each)
	
	/* 
	 * Core method, calls all the others (which are private)
	 * Makes a blank map, then keeps adding new blocks until you have reached the coveragePercentage
	 */
	public static Map generateMap (int width, int height, int coveragePercentage, int clusteringScore) {
		int totalSize = width*height;
		map = makeBlankMap(width, height);
		
		int coverageCounter = 0;
		int coverage = (coveragePercentage*totalSize)/100;

		//loop until we have as many blocks as the coverage indicated
		while(coverageCounter < coverage) {
			int locationInPotential = newLocationInPotentialSpace();			//this is the location within the potential space of the new block
			Coordinate coordinate = calculateCoordinate(locationInPotential, width, height);//this is the location (coordinate) in the spatial space of the  of the new block

			updatePotentialField(coordinate, width, height, clusteringScore);		//updates the potential field of the map
			
			coverageCounter++;
		}
		
		removeStartAndEnd(width, height);		// makes sure that the start and the end are unblocked
		
		return new Map(map);
	}

	private static int[][] makeBlankMap(int width, int height) {
		int[][] map = new int[width][height];
		for(int i=0; i < width; i++) {
			for(int j=0; j < height; j++) {
				map[i][j] = 1;
			}
		}
		totalPotential += width*height;			//updates the totalPotential to reflect that each cell has a potential of 1
		
		return map;
	}

	private static int newLocationInPotentialSpace() {
		return (int) Math.floor(Math.random()*totalPotential);
	}

	private static Coordinate calculateCoordinate(int locationInPotential, int width, int height) {
		
		int potentialCounter = map[0][0];

		int xCounter = 0;
		int yCounter = 0;
		while(potentialCounter < locationInPotential) {
			if((xCounter+1) < width) {
				xCounter++;
			} else {
				yCounter++;
				xCounter=0;
			}
			potentialCounter += map[xCounter][yCounter];
		}
		return new Coordinate(xCounter,yCounter);
	}

	private static void updatePotentialField(Coordinate coordinate, int width, int height, int clusteringScore) {
		int x = coordinate.getX();
		int y = coordinate.getY();
		if(map[x][y] != 0){					//if the cell is not already blocked
			totalPotential -= map[x][y];		//set the potential of the blocked cell to zero and reduce the total accordingly
			map[x][y] = 0;

			/*
			 * This is a very crude approximation of 1/r^2, where since the distance from the blocked cell to the adjacent
			 * cell is 1 and to its diagonal is sqrt(2), the 1^r2 rule says the potential increase at the adjacent cells should
			 * be twice that of in the diagonal cells. It completely ignores all other cells, but its a reasonable fast approximation
			 * that does the job.
			 * 
			 * Also I've been really lazy and just copy and pasted code for readability and ease. May clean this up later but it would
			 * require a new data structure or something and it's October so CBA
			 */
			
			Coordinate[] adjacentRelativeCoordinates = {new Coordinate(0,-1), new Coordinate(-1,0), new Coordinate(1,0), new Coordinate(0,1)}; // i.e. {above,left,right,below}
			for(int i=0; i<adjacentRelativeCoordinates.length; i++) {
				try {
					int relativeXCoordinate = adjacentRelativeCoordinates[i].getX();
					int relativeYCoordinate = adjacentRelativeCoordinates[i].getY();
					if(map[x + relativeXCoordinate][y + relativeYCoordinate] !=0) { //if the adjacent location doesn't contain a blocked cell
						map[x + relativeXCoordinate][y + relativeYCoordinate] += (2*clusteringScore);
						totalPotential += (2*clusteringScore);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					//skip if adjacent cell to be adjusted is already blocked
				}
			}
			
			Coordinate[] diagonalRelativeCoordinates = {new Coordinate(-1,-1), new Coordinate(-1,1), new Coordinate(1,-1), new Coordinate(1,1)}; // i.e. {above-left,above-right,below-left,below-right}
			for(int i=0; i<diagonalRelativeCoordinates.length; i++) {
				try {
					int relativeXCoordinate = diagonalRelativeCoordinates[i].getX();
					int relativeYCoordinate = diagonalRelativeCoordinates[i].getY();
					if(map[x + relativeXCoordinate][y + relativeYCoordinate] !=0) { //if the adjacent location doesn't contain a blocked cell
						map[x + relativeXCoordinate][y + relativeYCoordinate] += clusteringScore;
						totalPotential += clusteringScore;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					//skip if adjacent cell to be adjusted is already blocked
				}
			}
			
		} else {				//if there was already a blocked cell there
			//TODO should never get here, the fact that blocked cells have a potential of zero means this should never be reached. Throw an exception
		}
	}
	

	private static void removeStartAndEnd(int width, int height) {
		map[0][0] = 1;
		map[width-1][height-1] = 1;
	}
	
	public static void main(String[] args) {
		generateMap (60, 40, 28, 35);
	}
	
}
