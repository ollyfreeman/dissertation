package utility;

//there is a Java class Point for this, but it only uses doubles or floats
public class Coordinate implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;		
	
	private int x;
	private int y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}	
	
	public int hashCode() {
		return x*5 + y;		//is this ok - I am assuming Coordinate's hash value is only called when storing the LDDB
	}
	
	public boolean equals(Object o){
		Coordinate c = (Coordinate) o;
		if(this.x == c.x && this.y==c.y) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "("+this.x+","+this.y+")";
	}
}
