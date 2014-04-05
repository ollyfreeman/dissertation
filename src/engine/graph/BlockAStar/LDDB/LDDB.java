package engine.graph.BlockAStar.LDDB;

import java.util.ArrayList;

import engine.graph.BlockAStar.PairOfCoords;
import utility.Coordinate;

public interface LDDB{
	
	public float getLength(int mapCode, PairOfCoords p);
	
	public ArrayList<Coordinate> getIntermediateNodes(int mapCode, PairOfCoords p);
	
}