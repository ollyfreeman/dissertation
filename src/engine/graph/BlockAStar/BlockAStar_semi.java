package engine.graph.BlockAStar;

import java.util.ArrayList;
import java.util.List;

import utility.Coordinate;
import engine.graph.Graph;
import engine.graph.Node;
import engine.graph.BlockAStar.LDDB.bitwise.PairOfCoords_bitwise;
import engine.graph.BlockAStar.LDDB.geometric.Block_geometric;
import engine.graph.BlockAStar.LDDB.uncompressed.PairOfCoords_uncompressed;
import engine.map.Map;

public class BlockAStar_semi extends BlockAStar_standard {

	protected static final long serialVersionUID = 1L;
	
	public BlockAStar_semi(Map map, Coordinate source, Coordinate goal) {
		super(map,source,goal);	
	}

	@Override
	protected Block initStart(Block[][] blockArray, Map map) {
		Block startBlock = blockArray[this.source.getX()/blockSize][this.source.getY()/blockSize];
		startInBlock = new Coordinate(this.source.getX()%blockSize,this.source.getY()%blockSize);
		startBlock.setHeapValue(0);        //doesn't actually matter what this is
		for(int i=0; i<blockSize;i++) {
			Coordinate[] outArray ={new Coordinate(i,0),new Coordinate(blockSize,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};//Coordinate[] outArray = {new Coordinate(i,0),new Coordinate(0,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};
			for(Coordinate c : outArray) {
				double length;
				ArrayList<Coordinate> intermediateNodes;
				switch(compressionType) {
					case uncompressed: 	
						{length = lddb.getLength(startBlock.getCode(),new PairOfCoords_uncompressed(startInBlock,c));
						intermediateNodes = lddb.getIntermediateNodes(startBlock.getCode(),(new PairOfCoords_uncompressed(startInBlock,c)));}
						break;
					case bitwise: 		
						{length = lddb.getLength(startBlock.getCode(),new PairOfCoords_bitwise(startInBlock,c));
						intermediateNodes = lddb.getIntermediateNodes(startBlock.getCode(),(new PairOfCoords_bitwise(startInBlock,c)));}
						break;
					case geometric:		
						{Block_geometric currBlock = (Block_geometric) startBlock;
						length = lddb.getLength(currBlock.getCode(),(new PairOfCoords_uncompressed(currBlock.toRotated(startInBlock),currBlock.toRotated(c))));
						intermediateNodes = lddb.getIntermediateNodes(currBlock.getCode(),(new PairOfCoords_uncompressed(currBlock.toRotated(startInBlock),currBlock.toRotated(c))));
						ArrayList<Coordinate> newIntermediateNodes = new ArrayList<Coordinate>();
						for(Coordinate coord : intermediateNodes) {
							newIntermediateNodes.add(currBlock.toRotated(coord));
						}
						intermediateNodes = newIntermediateNodes;}
						break;
					case geobit:		
						{Block_geometric currBlock = (Block_geometric) startBlock;
						length = lddb.getLength(currBlock.getCode(),(new PairOfCoords_bitwise(currBlock.toRotated(startInBlock),currBlock.toRotated(c))));
						intermediateNodes = lddb.getIntermediateNodes(currBlock.getCode(),(new PairOfCoords_bitwise(currBlock.toRotated(startInBlock),currBlock.toRotated(c))));
						ArrayList<Coordinate> newIntermediateNodes = new ArrayList<Coordinate>();
						for(Coordinate coord : intermediateNodes) {
							newIntermediateNodes.add(currBlock.toRotated(coord));
						}
						intermediateNodes = newIntermediateNodes;}
						break;
					default:			
						length=0.0;
						intermediateNodes=null;
				}
				startBlock.setGValue(c, length);
				if(!c.equals(startInBlock)) {
					Node n = startBlock.getNode(c);
					for(Coordinate c1 : intermediateNodes) {
						n.setParent(startBlock.getNode(c1));
						n = startBlock.getNode(c1);
					}
					n.setParent(startBlock.getNode(startInBlock));
				}
			}
		}
		return startBlock;
	}

	@Override
	protected Block initGoal(Block[][] blockArray, Map map) {
		Block goalBlock;
		int x,y;
		if(this.goal.equals(new Coordinate(0,0))) {
			goalBlock = blockArray[0][0];	//-1 to make it 1 block closer to start if on an edge - to stop index out of bounds if in bottom right
			x=0;
			y=0;
		} else if (this.goal.equals(new Coordinate(map.getWidth(),map.getHeight()))){
			goalBlock = blockArray[blockArray.length-1][blockArray[0].length-1];
			x = this.goal.getX()%blockSize != 0 ? this.goal.getX()%blockSize : blockSize;
			y = this.goal.getY()%blockSize != 0 ? this.goal.getY()%blockSize : blockSize;
		} else {
			goalBlock = blockArray[(this.goal.getX())/blockSize][(this.goal.getY())/blockSize];
			x = this.goal.getX()%blockSize;
			y = this.goal.getY()%blockSize;
		}
		goalInBlock = new Coordinate(x,y);
		for(int i=0; i<blockSize;i++) {
			Coordinate[] outArray ={new Coordinate(i,0),new Coordinate(blockSize,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};//Coordinate[] outArray = {new Coordinate(i,0),new Coordinate(0,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};
			for(Coordinate c : outArray) {
				double length;
				ArrayList<Coordinate> intermediateNodes;
				switch(compressionType) {
					case uncompressed: 	
						{length = lddb.getLength(goalBlock.getCode(),new PairOfCoords_uncompressed(goalInBlock,c));
						intermediateNodes = lddb.getIntermediateNodes(goalBlock.getCode(),(new PairOfCoords_uncompressed(goalInBlock,c)));}
						break;
					case bitwise: 	
						{length = lddb.getLength(goalBlock.getCode(),new PairOfCoords_bitwise(goalInBlock,c));
						intermediateNodes = lddb.getIntermediateNodes(goalBlock.getCode(),(new PairOfCoords_bitwise(goalInBlock,c)));}
						break;
					case geometric:		
						{Block_geometric currBlock = (Block_geometric) goalBlock;
						length = lddb.getLength(currBlock.getCode(),(new PairOfCoords_uncompressed(currBlock.toRotated(goalInBlock),currBlock.toRotated(c))));
						intermediateNodes = lddb.getIntermediateNodes(currBlock.getCode(),(new PairOfCoords_uncompressed(currBlock.toRotated(goalInBlock),currBlock.toRotated(c))));
						ArrayList<Coordinate> newIntermediateNodes = new ArrayList<Coordinate>();
						for(Coordinate coord : intermediateNodes) {
							newIntermediateNodes.add(currBlock.toRotated(coord));
						}
						intermediateNodes = newIntermediateNodes;}
						break;
					case geobit:		
						{Block_geometric currBlock = (Block_geometric) goalBlock;
						length = lddb.getLength(currBlock.getCode(),(new PairOfCoords_bitwise(currBlock.toRotated(goalInBlock),currBlock.toRotated(c))));
						intermediateNodes = lddb.getIntermediateNodes(currBlock.getCode(),(new PairOfCoords_bitwise(currBlock.toRotated(goalInBlock),currBlock.toRotated(c))));
						ArrayList<Coordinate> newIntermediateNodes = new ArrayList<Coordinate>();
						for(Coordinate coord : intermediateNodes) {
							newIntermediateNodes.add(currBlock.toRotated(coord));
						}
						intermediateNodes = newIntermediateNodes;}
						break;
					default:			length=0.0;
										intermediateNodes=null;
				}
				goalBlock.setHValue(c, length);
				if(!c.equals(goalInBlock)) {
					Node n = goalBlock.getNode(goalInBlock);
					for(int j=intermediateNodes.size()-1;j>=0;j--) {
						Coordinate c1 = intermediateNodes.get(j);
						n.setParent(goalBlock.getNode(c1));
						n = goalBlock.getNode(c1);
					}
					n.setParent(goalBlock.getNode(c));
				}
			}
		}
		return goalBlock;
	}
	
	@Override
	protected List<Coordinate> getGoalIntermediateNodes(Graph graph, Map map, Block goalBlock) {
		return lddb.getIntermediateNodes(goalBlock.getCode(),(new PairOfCoords_uncompressed(graph.getSource().getCoordinate(),graph.getGoal().getCoordinate())));
	}
}