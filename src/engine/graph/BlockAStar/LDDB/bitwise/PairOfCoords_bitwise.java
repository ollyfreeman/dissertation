package engine.graph.BlockAStar.LDDB.bitwise;

import engine.graph.BlockAStar.PairOfCoords;
import utility.Coordinate;

public class PairOfCoords_bitwise extends PairOfCoords{
	
	private static final long serialVersionUID = 1L;
	
	private final int coordCode;
	private final Coordinate c1;
	private final Coordinate c2;
	
	public PairOfCoords_bitwise(Coordinate c1, Coordinate c2) {
		super(c1,c2);
		this.c1 = c1;
		this.c2 = c2;
		this.coordCode = getCode(c1,c2);
	}
	
	public Coordinate get1() {
		return c1;
	}
	
	public Coordinate get2() {
		return c2;
	}
	
	private int getCode(Coordinate c1, Coordinate c2) {
		int code = 0;
		code = code | c1.getX();
		code = code << 3;
		code = code | c1.getY();
		code = code << 3;
		code = code | c2.getX();
		code = code << 3;
		code = code | c2.getY();
		return code;	
	}
	
	public int hashCode() {
		return coordCode;
	}

	public boolean equals(Object o){
		PairOfCoords p = (PairOfCoords) o;
		if(this.hashCode() == p.hashCode()) {
			return true;
		} else {
		return false;
		}
	}
	
	@Override
	public String toString() {
		int temp = coordCode;
		int y2 = 7 & temp;
		temp = temp >>> 3;
		int x2 = 7 & temp;
		temp = temp >>> 3;
		int y1 = 7 & temp;
		temp = temp >>> 3;
		int x1 = 7 & temp;
		return "("+x1+","+y1+"),("+x2+","+y2+")";
	}
}
