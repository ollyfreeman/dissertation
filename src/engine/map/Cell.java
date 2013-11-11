package engine.map;

import utility.Coordinate;

/*
 * represents a map cell
 */
public class Cell {
	
	private final Coordinate coordinate;
	private final boolean isBlocked;

	public Cell(Coordinate coordinate, boolean blocked) {
		super();
		this.coordinate = coordinate;
		this.isBlocked = blocked;
	}
	
	public boolean isBlocked() {
		return isBlocked;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}
	
	public int getX() {
		return coordinate.getX();
	}
	
	public int getY() {
		return coordinate.getY();
	}
	
}
