package preparation;

//this class is in some dire need of some design patterns

public class MapGenerator {

	private static char[][] map;					
	private static int totalPotential = 0; 		//this is a sum of the potential scores on the entire map (initialised with 1 each)
	
	/* 
	 * Core method, calls all the others (which are private)
	 * Makes a blank map, then keeps adding new blocks until you have reached the coveragePercentage
	 */
	public static void generateMap (int width, int height, int coveragePercentage, int clusteringScore) {
		int totalSize = width*height;
		map = makeBlankMap(width, height);
		
		int coverageCounter = 0;
		int coverage = (coveragePercentage*totalSize)/100;

		//loop until we have as many blocks as the coverage indicated
		while(coverageCounter < coverage) {
			int locationInPotential = newLocationInPotentialSpace();			//this is the location within the potential space of the new block
			Location location = findLocation(locationInPotential, width, height);//this is the location in the spatial space of the  of the new block

			updatePotentialField(location, width, height, clusteringScore);		//updates the potential field of the map
			
			coverageCounter++;
		}
		
		removeStartAndEnd(width, height);		// makes sure that the start and the end are unblocked
		
		printMap(width, height);

	}

	private static char[][] makeBlankMap(int width, int height) {
		char[][] map = new char[width][height];
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

	private static Location findLocation(int locationInPotential, int width, int height) {
		
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
		return new Location(xCounter,yCounter);
	}

	private static void updatePotentialField(Location location, int width, int height, int clusteringScore) {
		int x = location.getX();
		int y = location.getY();
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
			
			Location[] adjacentRelativeLocations = {new Location(0,-1), new Location(-1,0), new Location(1,0), new Location(0,1)}; // i.e. {above,left,right,below}
			for(int i=0; i<adjacentRelativeLocations.length; i++) {
				try {
					int relativeXLocation = adjacentRelativeLocations[i].getX();
					int relativeYLocation = adjacentRelativeLocations[i].getY();
					if(map[x + relativeXLocation][y + relativeYLocation] !=0) { //if the adjacent location doesn't contain a blocked cell
						map[x + relativeXLocation][y + relativeYLocation] += (2*clusteringScore);
						totalPotential += (2*clusteringScore);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					//skip if adjacent cell to be adjusted is already blocked
				}
			}
			
			Location[] diagonalRelativeLocations = {new Location(-1,-1), new Location(-1,1), new Location(1,-1), new Location(1,1)}; // i.e. {above-left,above-right,below-left,below-right}
			for(int i=0; i<diagonalRelativeLocations.length; i++) {
				try {
					int relativeXLocation = diagonalRelativeLocations[i].getX();
					int relativeYLocation = diagonalRelativeLocations[i].getY();
					if(map[x + relativeXLocation][y + relativeYLocation] !=0) { //if the adjacent location doesn't contain a blocked cell
						map[x + relativeXLocation][y + relativeYLocation] += clusteringScore;
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

	private static void printMap(int width, int height) {
		for(int y=0;y<height; y++) {
			for(int x=0; x<width; x++) {
				if(map[x][y] == 0) {
					System.out.print("X ");
				} else {
					System.out.print("- ");
				}
			}
			System.out.print("\n");
		}
	}
	
	public static void main(String[] args) {
		generateMap (40, 40, 30, 50);
	}
	
}
