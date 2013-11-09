package preparation;

public class Cell {
	
	private final Coordinate coordinate;
	private final boolean isBlocked;
	private boolean isPath = false; //NOT GOING TO DO THIS IN THE FINAL VERSION

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
	
	public boolean isPath() {
		return isPath;
	}

	public void setPath(boolean b) {
		this.isPath = b;
	}
}
