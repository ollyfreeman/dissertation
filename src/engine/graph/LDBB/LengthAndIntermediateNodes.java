package engine.graph.LDBB;

import java.util.LinkedList;

import utility.Coordinate;

public class LengthAndIntermediateNodes implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private final double length;
	private final LinkedList<Coordinate> intermediateNodes;
	
	public LengthAndIntermediateNodes(double length, LinkedList<Coordinate> intermediateNodes) {
		this.length = length;
		this.intermediateNodes = intermediateNodes;
	}

	public double getLength() {
		return length;
	}

	public LinkedList<Coordinate> getIntermediateNodes() {
		return intermediateNodes;
	}
}
