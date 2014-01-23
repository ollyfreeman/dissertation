package engine.map;

import utility.Coordinate;

/*
 * generates map using the 'potential method' that I devised
 */
public class MapGenerator {

	private static int[][] map;							
	private static int totalPotential; 		//this is a sum of the potential scores on the entire map (initialised with 1 each)
	
	/* 
	 * Core method, calls all the others (which are private)
	 * Makes a blank map, then keeps adding new blocks until you have reached the coveragePercentage
	 */
	public static Map generateMap (int width, int height, int coveragePercentage, int clusteringScore) {
		totalPotential = 0;
		int totalSize = width*height;
		map = makeBlankMap(width, height);
		
		clusteringScore	= (clusteringScore*width*height)/4000;					//clustering weight is scaled according to resolution
		
		int coverageCounter = 0;
		int coverage = (coveragePercentage*totalSize)/100;

		//loop until we have as many blocked cells as the coveragePercentage indicated
		while(coverageCounter < coverage) {
			int locationInPotential = newLocationInPotentialSpace();			//this is the location within the potential space of the new block
			Coordinate coordinate = calculateCoordinate(locationInPotential, width, height);//this is the location (coordinate) in the spatial space of the  of the new block

			updatePotentialField(coordinate, width, height, clusteringScore);		//updates the potential field of the map
			
			coverageCounter++;
		}
		
		//removeStartAndEnd(width, height);		// makes sure that the start and the end are unblocked
		
		return new Map(map);
	}

	/*
	 * initialise a 2D array where each location is equally likely to be chosen (i.e. each location has a score of 1)
	 */
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

	/*
	 * Return a random number in the range 0 --> totalPotential
	 */
	private static int newLocationInPotentialSpace() {
		return (int) Math.floor(Math.random()*totalPotential);
	}

	/*
	 * calculate the coordinate in 2D space that corresponds to the locationInPotentialSpace in totalPotential 
	 */
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

	/*
	 * This takes the location of the newly generated blocked cell and then updates the potential map accordingly
	 * which involves setting the location that became blocked to have potential=0 (so that it can't be chosen again)
	 * and then adding potential (proportional to the clustering score) to the surrounding cells
	 */
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
			 * 
			 * In dissertation, I could compare this to Power Laws in graphs (see PoC) as opposed to
			 * potential fields?
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
	
	/*
	 * makes sure that the start and the end are unblocked
	 */
	private static void removeStartAndEnd(int width, int height) {
		map[0][0] = 1;
		map[width-1][height-1] = 1;
	}
	
}
