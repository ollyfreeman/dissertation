package engine.graph.BlockAStar;

import utility.Coordinate;

public abstract class PairOfCoords implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	public PairOfCoords(Coordinate c1, Coordinate c2, int blockSize) {};
	
	public abstract int hashCode();

	public abstract boolean equals(Object o);
	
	@Override
	public abstract String toString();
}
