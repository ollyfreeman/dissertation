package engine.graph.BlockAStar;

import java.util.ArrayList;

import utility.Coordinate;
import engine.graph.Node;
import engine.graph.BlockAStar.LDDB.bitwise.PairOfCoords_bitwise;
import engine.graph.BlockAStar.LDDB.geometric.Block_geometric;
import engine.graph.BlockAStar.LDDB.uncompressed.PairOfCoords_uncompressed;
import engine.map.Map;

public class BlockAStar_full extends BlockAStar_semi {

	private static final long serialVersionUID = 1L;
	
	protected static String filename = "/Users/olly_freeman/Dropbox/Part2Project/full";
	
	public BlockAStar_full(Map map, Coordinate source, Coordinate goal) {
		super(map,source,goal);	
	}

	@Override
	protected Node startAndGoalInSameBlock(Block startBlock, Block goalBlock, Map map) {
		double length;
		ArrayList<Coordinate> intermediateNodes;
		switch(compressionType) {
			case uncompressed: 	
				length = lddb.getLength(goalBlock.getCode(),new PairOfCoords_uncompressed(startInBlock,goalInBlock));
				intermediateNodes = lddb.getIntermediateNodes(goalBlock.getCode(),(new PairOfCoords_uncompressed(startInBlock,goalInBlock)));
				break;
			case bitwise: 		
				length = lddb.getLength(goalBlock.getCode(),new PairOfCoords_bitwise(startInBlock,goalInBlock));
				intermediateNodes = lddb.getIntermediateNodes(goalBlock.getCode(),(new PairOfCoords_bitwise(startInBlock,goalInBlock)));
				break;
			case geometric: 	
				Block_geometric currBlock = (Block_geometric) startBlock;
				length = lddb.getLength(goalBlock.getCode(),new PairOfCoords_uncompressed(currBlock.toRotated(startInBlock),currBlock.toRotated(goalInBlock)));
				intermediateNodes = lddb.getIntermediateNodes(goalBlock.getCode(),(new PairOfCoords_uncompressed(currBlock.toRotated(startInBlock),currBlock.toRotated(goalInBlock))));
				ArrayList<Coordinate> newIntermediateNodes = new ArrayList<Coordinate>();
				for(Coordinate coord : intermediateNodes) {
					newIntermediateNodes.add(currBlock.toRotated(coord));
				}
				intermediateNodes = newIntermediateNodes;
				break;
			case geobit: 	
				currBlock = (Block_geometric) startBlock;
				length = lddb.getLength(goalBlock.getCode(),new PairOfCoords_bitwise(currBlock.toRotated(startInBlock),currBlock.toRotated(goalInBlock)));
				intermediateNodes = lddb.getIntermediateNodes(goalBlock.getCode(),(new PairOfCoords_bitwise(currBlock.toRotated(startInBlock),currBlock.toRotated(goalInBlock))));
				newIntermediateNodes = new ArrayList<Coordinate>();
				for(Coordinate coord : intermediateNodes) {
					newIntermediateNodes.add(currBlock.toRotated(coord));
				}
				intermediateNodes = newIntermediateNodes;
				break;
			default:			
				length=0.0;
								
				intermediateNodes=null;
		}
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