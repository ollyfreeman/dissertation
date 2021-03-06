package engine.map;

import utility.Coordinate;

/*
 * represents a map
 */
public class Map implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
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
	
	//for creating submaps from maps
	public Map(Map m, Coordinate topLeft, int width, int height) {
		this.map = new Cell[width][height];
		
		for(int j=0; j< height; j++) {
			for(int i=0; i< width; i++) {
				try {
					this.map[i][j] = m.map[topLeft.getX()+i][topLeft.getY()+j];
				} catch (ArrayIndexOutOfBoundsException e) {
					this.map[i][j] = new Cell(new Coordinate(i,j),true);
				}
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
	
	//for testing only
	public void print() {
		for(int j=0; j<map[0].length; j++) {
			for(int i=0;i<map.length;i++) {
				if(map[i][j].isBlocked()) {
					System.out.print("X");
				} else {
					System.out.print("-");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

}
