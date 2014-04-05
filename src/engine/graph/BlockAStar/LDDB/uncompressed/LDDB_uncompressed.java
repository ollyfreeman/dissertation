package engine.graph.BlockAStar.LDDB.uncompressed;

import java.util.ArrayList;
import java.util.HashMap;

import engine.graph.BlockAStar.PairOfCoords;
import engine.graph.BlockAStar.LDDB.LDDB;
import utility.Coordinate;
import utility.Pair;

public class LDDB_uncompressed implements LDDB,java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final ArrayList<HashMap<PairOfCoords_uncompressed,Pair<Float,ArrayList<Coordinate>>>> list;
	
	public LDDB_uncompressed(ArrayList<HashMap<PairOfCoords_uncompressed,Pair<Float,ArrayList<Coordinate>>>> list) {
		this.list = list;
	}
	
	public float getLength(int mapCode, PairOfCoords p) {
			if(list.get(mapCode).get(p) == null) {
				return Float.POSITIVE_INFINITY;
			} else {
				return list.get(mapCode).get(p).get0();
			} 
	}
	
	public ArrayList<Coordinate> getIntermediateNodes(int mapCode, PairOfCoords p) {
		if(list.get(mapCode).get(p) != null) {
			return list.get(mapCode).get(p).get1();	
		} else {
			return new ArrayList<Coordinate>();
		}
	}


}