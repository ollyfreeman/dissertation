package engine.graph.BlockAStar.LDDB;

import utility.Coordinate;

public class PairOfCoords implements java.io.Serializable{
	
	private final short coordCode;
	private final int blockSize;
	
	public PairOfCoords(Coordinate c1, Coordinate c2, int blockSize) {
		this.blockSize = blockSize;
		this.coordCode = (short) (c1.getX() + (blockSize+2)*c1.getY() + (blockSize+2)*(blockSize+2)*c2.getX() + (blockSize+2)*(blockSize+2)*(blockSize+2)*c2.getY());
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
		int cC = coordCode;
		int x1 = cC%(blockSize+2);
		cC/=(blockSize+2);
		int y1 = cC%(blockSize+2);
		cC/=(blockSize+2);
		int x2 = cC%(blockSize+2);
		cC/=(blockSize+2);
		int y2 = cC%(blockSize+2);
		return "("+x1+","+y1+"),("+x2+","+y2+")";
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
