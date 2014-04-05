package engine.graph.BlockAStar.LDDB.geometric;

import java.util.ArrayList;
import java.util.HashMap;

import engine.graph.BlockAStar.PairOfCoords;
import engine.graph.BlockAStar.LDDB.LDDB;
import engine.graph.BlockAStar.LDDB.uncompressed.PairOfCoords_uncompressed;
import utility.Coordinate;
import utility.Pair;

public class LDDB_geometric implements LDDB,java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final ArrayList<HashMap<PairOfCoords_uncompressed,Pair<Float,ArrayList<Coordinate>>>> list;
	private short[] conversionTable;
	
	public LDDB_geometric(ArrayList<HashMap<PairOfCoords_uncompressed,Pair<Float,ArrayList<Coordinate>>>> list, short[] conversionTable) {
		this.list = list;
		System.out.println("um?"+list.size());
		this.conversionTable = conversionTable;
	}
	
	public float getLength(int mapCode, PairOfCoords p) {
		int lookup = conversionTable[mapCode];
		if(list.get(lookup).get(p) == null) {
			return Float.POSITIVE_INFINITY;
		} else {
			return list.get(lookup).get(p).get0();
		} 
	}
	
	public ArrayList<Coordinate> getIntermediateNodes(int mapCode, PairOfCoords p) {
		int lookup = conversionTable[mapCode];
		if(list.get(lookup).get(p) != null) {
			return list.get(lookup).get(p).get1();	
		} else {
			return new ArrayList<Coordinate>();
		}
	}


}