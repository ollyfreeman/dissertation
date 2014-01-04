package preparation;

import java.util.List;
import java.util.LinkedList;

import utility.Coordinate;

public class Block implements Comparable<Block>{
	
	private double heapValue = Double.POSITIVE_INFINITY;
	private Coordinate bestIngress= null;		//corresponds to heap value - used for traceback
	private Coordinate bestEgress = null;		//used for traceback
	private Block parent = null;		//used for traceback
	private Coordinate topLeft;
	private double[][] gArray;
	private double[][] hArray;
	private LinkedList<Block> neighbours = new LinkedList<Block>();
	private int code;
	
	public Block(int code, int size, Coordinate topLeft, Coordinate goal) {
		this.code = code;
		this.topLeft = topLeft;
		gArray = new double[size+1][size+1];
		heapValue = Double.POSITIVE_INFINITY;
		for(int i=0;i<=size;i++) {
			for(int j=0;j<=size;j++) {
				gArray[i][j] = Double.POSITIVE_INFINITY;
			}
		}
		for(int i=topLeft.getX();i<=size;i++) {
			for(int j=topLeft.getY();j<=size;j++) {
				hArray[i][j] = getDistance(i,j,goal);
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
	
	public void setBestIngress(Coordinate c, double value) {
		this.bestIngress = c;
		this.heapValue = value;
	}
	
	public void setBestEgress(Coordinate c) {
		this.bestEgress = c;
	}
	
	public Coordinate getBestIngress() {
		return this.bestIngress;
	}
	
	public Coordinate getBestEgress() {
		return this.bestEgress;
	}
	
	public List<Coordinate> getIngressNodes() {
		LinkedList<Coordinate> result = new LinkedList<Coordinate>();
		for(int j=0;j<=gArray[0].length;j++) {
			for(int i=0;i<=gArray.length;i++) {
				if(gArray[i][j] < 0) {
					result.add(new Coordinate(i,j));
					gArray[i][j] = (-1)*gArray[i][j];
				}
			}
		}
		return result;
	}
	
	public void setGValue(Coordinate c, double value) {
		gArray[c.getX()][c.getY()] = (-1)*value;
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
	
	public void setParent(Block parent) {
		this.parent = parent;
	}
	
	public Block getParent() {
		return this.parent;
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
