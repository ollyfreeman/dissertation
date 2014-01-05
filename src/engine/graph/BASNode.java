package engine.graph;

import utility.Coordinate;

/*
 * represents the node on a connected graph
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
