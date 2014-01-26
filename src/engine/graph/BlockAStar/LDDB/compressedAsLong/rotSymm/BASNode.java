package engine.graph.BlockAStar.LDDB.compressedAsLong.rotSymm;

import engine.graph.Node;
import utility.Coordinate;

/*
 * used in block A* - needed to identify which block a node is 
 * when inserting intermediate nodes during traceback
 */

public class BASNode extends engine.graph.Node{
	
	private static final long serialVersionUID = 1L;
	
	private final Block block;

	public BASNode(Coordinate coordinate, Block block) {
		super(coordinate);
		this.block = block;
	}

	public Block getBlock() {
		return this.block;
	}
}
