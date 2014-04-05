package engine.graph.BlockAStar.LDDB.bitwise;

import java.util.ArrayList;
import java.util.HashMap;

import engine.graph.BlockAStar.PairOfCoords;
import engine.graph.BlockAStar.LDDB.LDDB;
import utility.Coordinate;

public class LDDB_bitwise implements LDDB,java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final ArrayList<HashMap<Integer,Long>> list;
	
	public LDDB_bitwise(ArrayList<HashMap<Integer,Long>> list) {
		this.list = list;
	}
	
	public float getLength(int mapCode, PairOfCoords p) {
			if(list.get(mapCode).get(p.hashCode()) == null) {
				return Float.POSITIVE_INFINITY;
			} else {
				long l = list.get(mapCode).get(p.hashCode());
				l = l >>> 32;
				return Float.intBitsToFloat((int) l);
			} 
	}
	
	public ArrayList<Coordinate> getIntermediateNodes(int mapCode, PairOfCoords p) {
		ArrayList<Coordinate> aL = new ArrayList<Coordinate>();
		if(list.get(mapCode).get(p.hashCode())	!= null) {
			int listCode = (int) (long) list.get(mapCode).get(p.hashCode());;
			while(listCode !=0) {	//we will never have (0,0) as a coordinate in intermediate nodes so this is OK
				int y = listCode & 7;
				listCode = listCode >>> 3;
				int x = listCode & 7;
				aL.add(0, new Coordinate(x,y));
				listCode = listCode >>> 3;
			}
		}
		return aL;
	}
	
	public static int getListCode(ArrayList<Coordinate> aL) {
		int listCode = 0;
		for(Coordinate c : aL) {
			listCode = listCode | c.getX();
			listCode = listCode << 3;
			listCode = listCode | c.getY();
			listCode = listCode << 3;
		}
		listCode = listCode >>> 3;	//undo final listCode = listCode << 3;
		return listCode;
	}


}