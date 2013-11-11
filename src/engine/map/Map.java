package engine.map;

import utility.Coordinate;

/*
 * represents a map
 */
public class Map {
	
	private Cell[][] map;
	
	/*
	 * constructor creates a map from a 2D array of ints, where an array location 
	 * with value of 0 represents a blocked cell, all other cells are unblocked
	 * This is used for map creation from potential method
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
	
	//ONE OF THESE IS PROBS REDUNDANT
	public Cell getCell(Coordinate coordinate) {
		return map[coordinate.getX()][coordinate.getY()];
	}
	public Cell getCell(int x, int y) {
		return map[x][y];
	}
	//
	
	public int getWidth() {
		return map.length;
	}
	
	public int getHeight() {
		return map[0].length;
	}

}
