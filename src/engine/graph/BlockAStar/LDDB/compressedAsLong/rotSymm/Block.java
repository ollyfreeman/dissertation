package engine.graph.BlockAStar.LDDB.compressedAsLong.rotSymm;

import java.util.ArrayList;
import java.util.LinkedList;

import engine.graph.Node;
import engine.map.Map;
import utility.Coordinate;
import utility.Pair;

public class Block implements Comparable<Block>{
	
	private double heapValue = Double.POSITIVE_INFINITY;
	private Coordinate topLeft;
	private boolean[][] updatedGArray;
	private BASNode[][] nodeArray;
	private ArrayList<Block> neighbours = new ArrayList<Block>(8);
	private int code;
	private int rotation;
	protected int size;
	private ArrayList<LinkedList<Coordinate>> rotatedEgress;
	
	public Block(Map map, Coordinate topLeft, int size, Coordinate goal) {
		this.size=size;
		Pair<Integer,Integer> p = getCodeAndRotation(map,topLeft);
		this.code = p.get0();
		this.rotation = p.get1();
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
		makeRotatedEgress();
	}
	
	public LinkedList<Coordinate> getRotatedEgress(int i) {
		return rotatedEgress.get(i);
	}
	
	private void makeRotatedEgress() {
		rotatedEgress = new ArrayList<LinkedList<Coordinate>>(8);

		LinkedList<Coordinate> topLeft = new LinkedList<Coordinate>();
		topLeft.add(new Coordinate(0,0));
		rotatedEgress.add(0, topLeft);
		
		LinkedList<Coordinate> top = new LinkedList<Coordinate>();
		for(int i=1;i<this.size;i++) {
			top.add(new Coordinate(i,0));
		}
		rotatedEgress.add(1, top);
		
		LinkedList<Coordinate> topRight = new LinkedList<Coordinate>();
		topRight.add(new Coordinate(this.size,0));
		rotatedEgress.add(2, topRight);
		
		LinkedList<Coordinate> right = new LinkedList<Coordinate>();
		for(int i=1;i<this.size;i++) {
			right.add(new Coordinate(this.size,i));
		}
		rotatedEgress.add(3, right);
		
		LinkedList<Coordinate> bottomRight = new LinkedList<Coordinate>();
		bottomRight.add(new Coordinate(this.size,this.size));
		rotatedEgress.add(4, bottomRight);
		
		LinkedList<Coordinate> bottom = new LinkedList<Coordinate>();
		for(int i=1;i<this.size;i++) {
			bottom.add(new Coordinate(i,this.size));
		}
		rotatedEgress.add(5, bottom);
		
		LinkedList<Coordinate> bottomLeft = new LinkedList<Coordinate>();
		bottomLeft.add(new Coordinate(0,this.size));
		rotatedEgress.add(6, bottomLeft);
		
		LinkedList<Coordinate> left = new LinkedList<Coordinate>();
		for(int i=1;i<this.size;i++) {
			left.add(new Coordinate(0,i));
		}
		rotatedEgress.add(7, left);
		
		for(LinkedList<Coordinate> ll : rotatedEgress) {
			for(int i=0;i<ll.size();i++) {
				ll.set(i, adjustCoordinates(ll.get(i),true));
			}
		}
	}
	
	public int getCode() {
		return this.code;
	}
	
	public Coordinate adjustCoordinates(Coordinate c, boolean originalToDBRotation) {
		int x,y;
		if(originalToDBRotation) {
			x = c.getX();
			y = c.getY();
			for(int i=0;i<this.rotation;i++) {
				int newX = y;
				int newY = this.size-x;
				x = newX;
				y = newY;
			}
		} else {
			x = c.getX();
			y = c.getY();
			for(int i=0;i<this.rotation;i++) {
				int newX = this.size-y;
				int newY = x;
				x = newX;
				y = newY;
			}
		}
		return new Coordinate(x,y);
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
	
	public LinkedList<Coordinate> getIngressNodes() {
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
	
	private float getDistance(int x, int y, Coordinate goal) {
		float xDiff = x - goal.getX();
		float yDiff = y - goal.getY();
		return (float) Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}
	
	protected Pair<Integer,Integer> getCodeAndRotation(Map map, Coordinate topLeft) {
		int[][] m = new int[this.size][this.size];
		for(int i=0;i<this.size;i++) {
			for(int j=0;j<this.size;j++) {
				try {
					if(map.getCell(i+topLeft.getX(), j+topLeft.getY()).isBlocked()) {
						m[i][j] = 0;
					} else {
						m[i][j] = 1;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					m[i][j] = 0;
				}
			}
		}
		int minCode = getCodeFromMap(m);
		int minRotation = 0;
		for(int rot=1; rot<=3; rot++) {
			int[][] tempMap = new int[this.size][this.size];
			for(int i=0;i<this.size;i++) {
				for(int j=0;j<this.size;j++) {
					tempMap[i][j] = m[this.size-1-j][i];
				}
			}
			if(getCodeFromMap(tempMap)<minCode) {
				minCode = getCodeFromMap(tempMap);
				minRotation = rot;
			}
			m=tempMap;
		}
		return new Pair<Integer,Integer>(minCode,minRotation);
	}
	
	protected int getCodeFromMap(int[][] map) {
		int code = 0;
		for(int j=0;j<this.size;j++) {
			for(int i=0;i<this.size;i++) {
				code = code<<1;
				try{
					if(map[i][j] > 0) {
						code++;
						//System.out.println(i + "," + j + " is free");
					} else {
						//System.out.println(i + "," + j + " is blocked");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					//skip if part of this block is out of bounds, because we call those cells blocked!
				}
			}
		}
		return code;
	}

}
