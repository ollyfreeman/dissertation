package engine.graph;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import engine.map.Map;
import utility.Coordinate;
import engine.graph.Node;
import engine.graph.PairOfCoords;
import engine.graph.Block;

public class BlockAStarAlgorithm {

	private static PriorityQueue<Block> openSet = new PriorityQueue<Block>();
	private final static int blockSize = 2;
	private static LinkedList<HashMap<PairOfCoords,Double>> db = loadDB();
	
	public static Node getPath(Map map) {
		int blockArrayWidth = (int) Math.ceil((map.getWidth()+0.0)/blockSize);
		int blockArrayHeight = (int) Math.ceil((map.getHeight()+0.0)/blockSize);
		Block[][] blockArray = new Block[blockArrayWidth][blockArrayHeight];
		initBlockArray(blockArray, blockArrayWidth, blockArrayHeight,map);

		Block startBlock = blockArray[0][0];
		Block goalBlock = blockArray[blockArrayWidth-1][blockArrayHeight-1];//Block goalBlock = blockArray[(map.getWidth()/blockSize)-1][(map.getHeight()/blockSize)-1];
		
		initStart(startBlock,map);
		initGoal(goalBlock,map);
		double length = Double.POSITIVE_INFINITY;
		openSet.add(startBlock);
		
		while(!openSet.isEmpty() && openSet.peek().getHeapValue() < length) {
			Block currentBlock = openSet.remove();
			List<Coordinate> ingressNodes = currentBlock.getIngressNodes();
			if(currentBlock == goalBlock) {
				for(Coordinate c : ingressNodes) {
					if(goalBlock.getGValue(c)+goalBlock.getHValue(c) < length) {
						length = goalBlock.getGValue(c)+goalBlock.getHValue(c);
						//parent of goal is c
						goalBlock.setParent(new Coordinate(map.getWidth()-goalBlock.getTopLeft().getX(),map.getHeight()-goalBlock.getTopLeft().getY()), goalBlock.getNode(c));//goalBlock.setParent(new Coordinate(blockSize,blockSize), goalBlock.getNode(c));//goalBlock.setParent(new Coordinate(map.getWidth()-goalBlock.getTopLeft().getX(),map.getHeight()-goalBlock.getTopLeft().getY()), goalBlock.getNode(c));
					}
					
				}
			}
			expand(currentBlock,ingressNodes);
		}
		if(length!=Double.POSITIVE_INFINITY) {
			System.out.println("Found path of length " + length);
			return goalBlock.getNode(new Coordinate(map.getWidth()-goalBlock.getTopLeft().getX(),map.getHeight()-goalBlock.getTopLeft().getY()));//return goalBlock.getNode(new Coordinate(blockSize,blockSize));//return goalBlock.getNode(new Coordinate(map.getWidth()-goalBlock.getTopLeft().getX(),map.getHeight()-goalBlock.getTopLeft().getY()));
		} else {
			System.out.println("NO PATH!");
			return null;
		}
	}
	
	private static void expand(Block currentBlock, List<Coordinate> y) {
		for(Block neighbourBlock : currentBlock.getNeighbours()) {
			LinkedList<Coordinate> ListX = new LinkedList<Coordinate>();
			LinkedList<Coordinate> ListXPrime = new LinkedList<Coordinate>();
			Coordinate neighbourTL = neighbourBlock.getTopLeft();							//i.e. the topLeft coordinate of the neighbour
			Coordinate currentTL = currentBlock.getTopLeft();
			if(neighbourTL.getX() > currentTL.getX() && neighbourTL.getY() == currentTL.getY()) {
				for(int i=0;i<=blockSize;i++) {
					ListX.add(new Coordinate(blockSize,i));
					ListXPrime.add(new Coordinate(0,i));
				}
			} else if(neighbourTL.getX() < currentTL.getX() && neighbourTL.getY() == currentTL.getY()) {
				for(int i=0;i<=blockSize;i++) {
					ListX.add(new Coordinate(0,i));
					ListXPrime.add(new Coordinate(blockSize,i));
				}
			} else if(neighbourTL.getY() > currentTL.getY() && neighbourTL.getX() == currentTL.getX()) {
				for(int i=0;i<=blockSize;i++) {
					ListX.add(new Coordinate(i,blockSize));
					ListXPrime.add(new Coordinate(i,0));
				}
			} else if(neighbourTL.getY() < currentTL.getY() && neighbourTL.getX() == currentTL.getX()) {
				for(int i=0;i<=blockSize;i++) {
					ListX.add(new Coordinate(i,0));
					ListXPrime.add(new Coordinate(i,blockSize));
				}
			} else {
				System.out.println("Neighbour error!");
			}
			
			LinkedList<Coordinate> ListXPrimeUpdated = new LinkedList<Coordinate>();
			for(int i=0;i<ListX.size();i++) {
				Coordinate x = ListX.get(i);
				for(Coordinate c : y) {
					double length = db.get(currentBlock.getCode()).get(new PairOfCoords(x,c)) != null ? db.get(currentBlock.getCode()).get(new PairOfCoords(x,c)) : Double.POSITIVE_INFINITY;
					if(currentBlock.getGValue(c) + length < currentBlock.getGValue(x)) {
						currentBlock.setGValue(x, currentBlock.getGValue(c) + length);
						//parent of x is c
						currentBlock.setParent(x, currentBlock.getNode(c));
					}
				}
				Coordinate xPrime = ListXPrime.get(i);
				double xPrimeG = neighbourBlock.getGValue(xPrime);
				double xG = currentBlock.getGValue(x);
				if(xG < xPrimeG) {
					neighbourBlock.setGValue(xPrime,xG);
					ListXPrimeUpdated.add(xPrime);
					//parent of xPrime is x
					neighbourBlock.setParent(xPrime, currentBlock.getNode(x));
					
				}
			}
			double newHeapValue = Double.POSITIVE_INFINITY;
			for(Coordinate c : ListXPrimeUpdated) {
				if(neighbourBlock.getGValue(c)+neighbourBlock.getHValue(c) < newHeapValue) {
					newHeapValue = neighbourBlock.getGValue(c)+neighbourBlock.getHValue(c);
				}
			}
			if(newHeapValue < neighbourBlock.getHeapValue()) {
				neighbourBlock.setHeapValue(newHeapValue);
				if(!openSet.contains(neighbourBlock)) {
					openSet.add(neighbourBlock);
				}
			}
		}
	}
	
	private static int getMapCode(Coordinate topLeft, Map map) {
		int code = 0;
		for(int j=0;j<blockSize;j++) {
			for(int i=0;i<blockSize;i++) {
				code = code<<1;
				try{
					if(!map.getCell(i+topLeft.getX(),j+topLeft.getY()).isBlocked()) {
						code++;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					//skip if part of this block is out of bounds, because we call those cells blocked!
				}
			}
		}
		return code;
	}
	
	private static void initStart(Block block, Map map) {
		Coordinate startCoord = new Coordinate(0,0);
		block.setHeapValue(getDistance(new Coordinate(0,0),new Coordinate(map.getWidth(),map.getHeight())));	//doesn't actually matter what this is
		int code = getMapCode(startCoord,map);
		for(int i=0; i<blockSize;i++) {
			Coordinate[] outArray = {new Coordinate(blockSize,i),new Coordinate(blockSize-i,blockSize)};
			for(Coordinate c : outArray) {
				double length = db.get(code).get(new PairOfCoords(startCoord,c)) != null ? db.get(code).get(new PairOfCoords(startCoord,c)) : Double.POSITIVE_INFINITY;
				block.setGValue(c, length);
				block.setParent(c, block.getNode(startCoord));
			}
		}
	}
	
	private static void initGoal(Block block, Map map) {
		Coordinate endCoord = new Coordinate(map.getWidth()-block.getTopLeft().getX(),map.getHeight()-block.getTopLeft().getY());//Coordinate endCoord = new Coordinate(blockSize,blockSize);//Coordinate endCoord = new Coordinate(map.getWidth()-block.getTopLeft().getX(),map.getHeight()-block.getTopLeft().getY());
		System.out.println(endCoord.toString());
		int code = getMapCode(endCoord,map);
		for(int i=0; i<=blockSize;i++) {
			Coordinate[] outArray = {new Coordinate(0,i),new Coordinate(i,0)};
			for(Coordinate c : outArray) {
				double length = db.get(code).get(new PairOfCoords(c,endCoord)) != null ? db.get(code).get(new PairOfCoords(c,endCoord)) : Double.POSITIVE_INFINITY;
				block.setHValue(c, length);
			}
		}
	}
	
	private static void initBlockArray(Block[][] blockArray, int blockArrayWidth, int blockArrayHeight, Map map) {
		for(int j=0; j<blockArrayHeight;j++) {
			for(int i=0; i<blockArrayWidth;i++) {
				blockArray[i][j] = new Block(getMapCode(new Coordinate(i*blockSize,j*blockSize),map),blockSize,new Coordinate(i*blockSize,j*blockSize),new Coordinate(map.getWidth(),map.getHeight()));
			}
		}
		for(int j=0; j<blockArrayHeight;j++) {
			for(int i=0; i<blockArrayWidth;i++) {
				if(j!=0) {
					blockArray[i][j].addNeighbour(blockArray[i][j-1]);
				}
				if(j!=blockArrayHeight-1) {
					blockArray[i][j].addNeighbour(blockArray[i][j+1]);
				}
				if(i!=0) {
					blockArray[i][j].addNeighbour(blockArray[i-1][j]);
				}
				if(i!=blockArrayWidth-1) {
					blockArray[i][j].addNeighbour(blockArray[i+1][j]);
				}
			}
		}
	}
	
	private static LinkedList<HashMap<PairOfCoords,Double>> loadDB() {
		try {
			String filename = "/Users/olly_freeman/Dropbox/Part2Project/2by2db.ser";
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			LinkedList<HashMap<PairOfCoords,Double>> db = (LinkedList<HashMap<PairOfCoords,Double>>) in.readObject();
			in.close();
			fileIn.close();
			return db;
		} catch(IOException i) {
			i.printStackTrace();
			return null;
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			return null;
		}

	}

	private static double getDistance(Coordinate c1, Coordinate c2) {
		double xDiff = c1.getX() - c2.getX();
		double yDiff = c1.getY() - c2.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}

}
