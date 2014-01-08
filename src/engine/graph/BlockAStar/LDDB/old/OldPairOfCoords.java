package engine.graph.BlockAStar.LDDB.old;

import utility.Coordinate;

public class OldPairOfCoords implements java.io.Serializable{
	
	private final Coordinate c1;
	private final Coordinate c2;
	
	public OldPairOfCoords(Coordinate c1, Coordinate c2) {
			this.c1 = c1;
			this.c2 = c2;
		}
	
	public int hashCode() {
		return c1.getX() + (5)*c1.getY() + (5)*(5)*c2.getX() + (5)*(5)*(5)*c2.getY();
		
	}

	public boolean equals(Object o){
		OldPairOfCoords p = (OldPairOfCoords) o;
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


/*/public class PairOfCoords {
	
	private final Coordinate c1;
	private final Coordinate c2;
	
	public PairOfCoords(Coordinate c1, Coordinate c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public Coordinate getC1() {
		return this.c1;
	}
	
	public Coordinate getC2() {
		return this.c2;
	}
	
	public int hashCode() {
		return c1.getX() + 6*c1.getY() + 36*c2.getX() + 216*c2.getY();
	}

	public boolean equals(Object o){
		PairOfCoords p = (PairOfCoords) o;
		if(this.c1.getX() == p.getC1().getX() && this.c1.getY()==p.getC1().getY() && this.c2.getX() == p.getC2().getX() && this.c2.getY()==p.getC2().getY()) {
			return true;
		} else {
		return false;
		}
	}
*/
}
