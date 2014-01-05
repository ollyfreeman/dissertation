package engine.graph;

import java.util.List;
import java.util.LinkedList;

import utility.Coordinate;

public class Block implements Comparable<Block>{
	
	private double heapValue = Double.POSITIVE_INFINITY;
	private Coordinate topLeft;
	private double[][] gArray;
	private boolean[][] updatedGArray;
	private double[][] hArray;
	private BASNode[][] parentArray;
	private LinkedList<Block> neighbours = new LinkedList<Block>();
	private int code;
	
	public Block(int code, int size, Coordinate topLeft, Coordinate goal) {
		this.code = code;
		this.topLeft = topLeft;
		gArray = new double[size+1][size+1];
		updatedGArray = new boolean[size+1][size+1];
		hArray = new double[size+1][size+1];
		parentArray = new BASNode[size+1][size+1];
		heapValue = Double.POSITIVE_INFINITY;
		for(int i=0;i<=size;i++) {
			for(int j=0;j<=size;j++) {
				gArray[i][j] = Double.POSITIVE_INFINITY;
				updatedGArray[i][j] = false;
				hArray[i][j] = getDistance(topLeft.getX()+i,topLeft.getY()+j,goal);
				parentArray[i][j] = new BASNode(new Coordinate(this.topLeft.getX()+i,this.topLeft.getY()+j),this);
			}
		}
	}
	
	public int getCode() {
		return this.code;
	}
	
	public void addNeighbour(Block b) {
		this.neighbours.add(b);
	}
	
	public LinkedList<Block> getNeighbours() {
		return this.neighbours;
	}
	
	public Coordinate getTopLeft() {
		return this.topLeft;
	}
	
	public double getHeapValue() {
		return this.heapValue;
	}
	
	public void setHeapValue(double value) {
		this.heapValue = value;
	}
	
	public List<Coordinate> getIngressNodes() {
		LinkedList<Coordinate> result = new LinkedList<Coordinate>();
		for(int j=0;j<gArray[0].length;j++) {
			for(int i=0;i<gArray.length;i++) {
				if(updatedGArray[i][j]) {
					result.add(new Coordinate(i,j));
					updatedGArray[i][j] = false;
				}
			}
		}
		return result;
	}
	
	public void setGValue(Coordinate c, double value) {
		gArray[c.getX()][c.getY()] = value;
		updatedGArray[c.getX()][c.getY()]  = true;
	}
	
	public double getGValue(Coordinate c) {
		return gArray[c.getX()][c.getY()];
	}
	
	//only to be used if goal block - I should subclass block to have goal block and put this method in there
	public void setHValue(Coordinate c, double value) {
		hArray[c.getX()][c.getY()] = value;
	}
	
	public double getHValue(Coordinate c) {
		return hArray[c.getX()][c.getY()];
	}
	
	//sets the parent of the node (with Coordinate c) to Node n
	public void setParent(Coordinate c, Node n) {
		parentArray[c.getX()][c.getY()].setParent(n);
	}
	
	public BASNode getNode(Coordinate c) {
		return parentArray[c.getX()][c.getY()];
	}
	
	@Override
	/*
	 * Makes block comparable by heapValue, so they can be sorted in a priority queue
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Block b) {
		if(this.heapValue < b.heapValue) {
			return -1;
		} else if(this.heapValue > b.heapValue) {
			return 1;
		} else {
			return 0;
		}
	}
	
	private static double getDistance(int x, int y, Coordinate goal) {
		double xDiff = x - goal.getX();
		double yDiff = y - goal.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}

}
