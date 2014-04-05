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
}
