package engine.graph.BlockAStar.LDDB.geobit;

import java.util.ArrayList;
import java.util.HashMap;

import engine.graph.BlockAStar.PairOfCoords;
import engine.graph.BlockAStar.LDDB.LDDB;
import engine.graph.BlockAStar.LDDB.bitwise.PairOfCoords_bitwise;
import utility.Coordinate;

public class LDDB_geobit implements LDDB,java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final ArrayList<HashMap<Integer,Long>> list;
	private short[] conversionTable;
	
	public LDDB_geobit(ArrayList<HashMap<Integer,Long>> list, short[] conversionTable) {
		this.list = list;
		this.conversionTable = conversionTable;
	}
	
	public float getLength(int mapCode, PairOfCoords p) {
		if(!((p.get1().getY() < p.get2().getY()) || ((p.get1().getY() == p.get2().getY()) && p.get1().getX() < p.get2().getY()))) {
			p = new PairOfCoords_bitwise(p.get2(),p.get1()); 
		}
		int lookup = conversionTable[mapCode];
		if(list.get(lookup).get(p.hashCode()) == null) {
			return Float.POSITIVE_INFINITY;
		} else {
			long l = list.get(lookup).get(p.hashCode());
			l = l >>> 32;
			return Float.intBitsToFloat((int) l);
		} 
	}
	
	public ArrayList<Coordinate> getIntermediateNodes(int mapCode, PairOfCoords p) {
		if(!((p.get1().getY() < p.get2().getY()) || ((p.get1().getY() == p.get2().getY()) && p.get1().getX() < p.get2().getY()))) {
			p = new PairOfCoords_bitwise(p.get2(),p.get1()); 
		}
		int lookup = conversionTable[mapCode];
		ArrayList<Coordinate> aL = new ArrayList<Coordinate>();
		if(list.get(lookup).get(p.hashCode()) != null) {
			int listCode = (int) (long) list.get(lookup).get(p.hashCode());;
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


}