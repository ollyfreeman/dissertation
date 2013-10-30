package preparation;

public class Map {
	
	public Cell[][] map; //will be made private! for testing only!!
	
	/*
	 * not convinced I should have this code in the constructor. I'd rather do like what I did for
	 * graph and be able to set it using setThing methods because I think it's closer to the factory pattern
	 * where the factory (what ive called generator) is doing the work...but I may be interpreting the 
	 * pattern incorrectly
	 */
	public Map(int[][] input) {
		map = new Cell[input.length][input[0].length];
		
		for(int j=0; j< input[0].length; j++) {
			for(int i=0; i< input.length; i++) {
				boolean isBlocked = (input[i][j] == 0) ? true : false;
				map[i][j] = new Cell(new Coordinate(i,j),isBlocked);
			}
		}
	}
	
	public Cell getCell(int x, int y) {
		return map[x][y];
	}
	//ONE OF THESE IS PROBS REDUNDANT
	public Cell getCell(Coordinate coordinate) {
		return map[coordinate.getX()][coordinate.getY()];
	}
	
	public int getWidth() {
		return map.length;
	}
	
	public int getHeight() {
		return map[0].length;
	}
	
	//this will ultimately be removed
	public void printMap() {
		for(int y=0;y < map[0].length; y++) {
			for(int x=0; x < map.length; x++) {
				if(map[x][y].isBlocked()) {
					System.out.print("- ");
				} else if(map[x][y].isPath()){
					System.out.print("o ");
				} else {
					System.out.print("  ");
				}
			}
			System.out.print("\n");
		}
		System.out.println();
	}

}
