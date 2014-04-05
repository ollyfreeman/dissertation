package engine.graph.BlockAStar.LDDB.naive;

import utility.Coordinate;

public class PairOfCoords_naive implements java.io.Serializable{
	
	private final Coordinate c1;
	private final Coordinate c2;
	
	public PairOfCoords_naive(Coordinate c1, Coordinate c2,int blockSize) {
			//blockSize is unused
			this.c1 = c1;
			this.c2 = c2;
		}

	public boolean equals(Object o){
		PairOfCoords_naive p = (PairOfCoords_naive) o;
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
	
	public int hashCode() {
		return c1.getX() + (5)*c1.getY() + (5)*(5)*c2.getX() + (5)*(5)*(5)*c2.getY();
		
	}
}
