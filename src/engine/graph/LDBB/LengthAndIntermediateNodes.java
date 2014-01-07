package engine.graph.LDBB;

import java.util.ArrayList;

import utility.Coordinate;

public class LengthAndIntermediateNodes implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private final double length;
	private final ArrayList<Coordinate> intermediateNodes;
	
	public LengthAndIntermediateNodes(double length, ArrayList<Coordinate> intermediateNodes) {
		this.length = length;
		this.intermediateNodes = intermediateNodes;
	}

	public double getLength() {
		return length;
	}

	public ArrayList<Coordinate> getIntermediateNodes() {
		return intermediateNodes;
	}
}
