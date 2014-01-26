package engine.graph.BlockAStar;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import engine.graph.Node;
import utility.Coordinate;

public class Block implements Comparable<Block>{
	
	private double heapValue = Double.POSITIVE_INFINITY;
	private Coordinate topLeft;
	private boolean[][] updatedGArray;
	private BASNode[][] nodeArray;
	private ArrayList<Block> neighbours = new ArrayList<Block>(8);
	private int code;
	private int size;
	
	public Block(int code, int size, Coordinate topLeft, Coordinate goal) {
		this.code = code;
		this.size=size;
		this.topLeft = topLeft;
		updatedGArray = new boolean[size+1][size+1];
		nodeArray = new BASNode[size+1][size+1];
		heapValue = Double.POSITIVE_INFINITY;
		for(int i=0;i<=size;i++) {
			for(int j=0;j<=size;j++) {
				updatedGArray[i][j] = false;
				nodeArray[i][j] = new BASNode(new Coordinate(this.topLeft.getX()+i,this.topLeft.getY()+j),this);
				setHValue(new Coordinate(i,j),(getDistance(this.topLeft.getX()+i,this.topLeft.getY()+j,goal)));
			}
		}
	}
	
	public int getCode() {
		return this.code;
	}
	
	public void addNeighbour(Block b) {
		this.neighbours.add(b);
	}
	
	public ArrayList<Block> getNeighbours() {
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
		for(int j=0;j<updatedGArray[0].length;j++) {
			for(int i=0;i<updatedGArray.length;i++) {
				if(updatedGArray[i][j]) {
					result.add(new Coordinate(i,j));
					updatedGArray[i][j] = false;
				}
			}
		}
		return result;
	}
	
	public void setGValue(Coordinate c, double value) {
		nodeArray[c.getX()][c.getY()].setG(value);
		updatedGArray[c.getX()][c.getY()]  = true;
	}
	
	public double getGValue(Coordinate c) {
		return nodeArray[c.getX()][c.getY()].getG();
	}
	
	//only to be used if goal block - I should subclass block to have goal block and put this method in there
	public void setHValue(Coordinate c, double value) {
		nodeArray[c.getX()][c.getY()].setF(value);		//im using f for h
	}
	
	public double getHValue(Coordinate c) {
		return nodeArray[c.getX()][c.getY()].getF();
	}
	
	//sets the parent of the node (with Coordinate c) to Node n
	public void setParent(Coordinate c, Node n) {
		nodeArray[c.getX()][c.getY()].setParent(n);
	}
	
	public BASNode getNode(Coordinate c) {
		return nodeArray[c.getX()][c.getY()];
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
