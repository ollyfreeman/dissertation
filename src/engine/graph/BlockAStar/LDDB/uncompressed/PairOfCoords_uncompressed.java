package engine.graph.BlockAStar.LDDB.uncompressed;

import engine.graph.BlockAStar.PairOfCoords;
import utility.Coordinate;

public class PairOfCoords_uncompressed extends PairOfCoords{
	
	private static final long serialVersionUID = 1L;
	
	private final Coordinate c1;
	private final Coordinate c2;
	
	public PairOfCoords_uncompressed(Coordinate c1, Coordinate c2) {
		//blockSize is unused
		super(c1,c2);
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public Coordinate get1() {
		return c1;
	}
	
	public Coordinate get2() {
		return c2;
	}
	
	public int hashCode() {
		return c1.getX() + (5)*c1.getY() + (5)*(5)*c2.getX() + (5)*(5)*(5)*c2.getY();
		
	}

	public boolean equals(Object o){
		PairOfCoords_uncompressed p = (PairOfCoords_uncompressed) o;
		if(this.hashCode() == p.hashCode()) {
			return true;
		} else {
		return false;
		}
	}
	
	@Override
	public String toString() {
		return "("+c1.getX()+","+c1.getY()+"),("+c2.getX()+","+c2.getY()+")";
	}
}
