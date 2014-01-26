package engine.graph.BlockAStar.LDDB;

import java.util.ArrayList;
import java.util.HashMap;

import utility.Coordinate;
import utility.Pair;

public class LDDB implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final ArrayList<HashMap<PairOfCoords,Pair<Double,Integer>>> list;
	
	public LDDB(ArrayList<HashMap<PairOfCoords,Pair<Double,Integer>>> list) {
		this.list = list;
	}
	
	public double getLength(int mapCode, PairOfCoords p) {
			if(list.get(mapCode).get(p) == null) {
				return Float.POSITIVE_INFINITY;
			} else {
				return list.get(mapCode).get(p).get0();
			} 
	}
	
	public ArrayList<Coordinate> getIntermediateNodes(int mapCode, PairOfCoords p) {
		ArrayList<Coordinate> aL = new ArrayList<Coordinate>();
		if(list.get(mapCode).get(p)	!= null) {
			int listCode = list.get(mapCode).get(p).get1();
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