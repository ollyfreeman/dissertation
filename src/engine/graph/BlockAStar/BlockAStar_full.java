package engine.graph.BlockAStar;

import java.util.ArrayList;

import utility.Coordinate;
import engine.graph.Node;
import engine.graph.BlockAStar.LDDB.PairOfCoords;
import engine.map.Map;

public class BlockAStar_full extends BlockAStar_semi {

	private static final long serialVersionUID = 1L;
	
	public BlockAStar_full(Map map, Coordinate source, Coordinate goal) {
		super(map,source,goal);	
	}

	@Override
	protected Node startAndGoalInSameBlock(Block startBlock, Block goalBlock, Map map) {
		double length = lddb.getLength(goalBlock.getCode(),new PairOfCoords(startInBlock,goalInBlock,blockSize));
		ArrayList<Coordinate> intermediateNodes = lddb.getIntermediateNodes(goalBlock.getCode(),(new PairOfCoords(startInBlock,goalInBlock,blockSize)));
		if(length != Double.POSITIVE_INFINITY) {
			Node n = goalBlock.getNode(goalInBlock);
			if(!startInBlock.equals(goalInBlock)) {
				for(Coordinate c1 : intermediateNodes) {
					n.setParent(goalBlock.getNode(c1));
					n = goalBlock.getNode(c1);
				}
				n.setParent(goalBlock.getNode(startInBlock));
			}
			return goalBlock.getNode(goalInBlock);
		} else {
			return null;
		}
	}
}