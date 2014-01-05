package engine.graph;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import engine.map.Map;
import utility.Coordinate;
import engine.graph.BASNode;
import engine.graph.Block;
import engine.graph.LDBB.LengthAndIntermediateNodes;
import engine.graph.LDBB.PairOfCoords;

public class BlockAStarAlgorithm_v2 {

	private static PriorityQueue<Block> openSet = new PriorityQueue<Block>();
	private final static int blockSize = 4;
	private static ArrayList<HashMap<PairOfCoords,LengthAndIntermediateNodes>> db = loadDB();
	private static Map map;
	
	public static Node getPath(Map m) {
		map = m;
		int blockArrayWidth = (int) Math.ceil((map.getWidth()+0.0)/blockSize);
		int blockArrayHeight = (int) Math.ceil((map.getHeight()+0.0)/blockSize);
		Block[][] blockArray = new Block[blockArrayWidth][blockArrayHeight];
		initBlockArray(blockArray, blockArrayWidth, blockArrayHeight);

		Block startBlock = blockArray[0][0];
		Block goalBlock = blockArray[blockArrayWidth-1][blockArrayHeight-1];//Block goalBlock = blockArray[(map.getWidth()/blockSize)-1][(map.getHeight()/blockSize)-1];
		
		initStart(startBlock);
		initGoal(goalBlock);
		double length = Double.POSITIVE_INFINITY;
		openSet.add(startBlock);
		
		while(!openSet.isEmpty() && openSet.peek().getHeapValue() < length) {
			Block currentBlock = openSet.remove();
			/*if(currentBlock.getTopLeft().equals(new Coordinate(98,96))) {
				for(Coordinate c : currentBlock.getIngressNodes()) {
					System.out.print(c.toString());
				}
				System.out.println();
			}
			if(currentBlock.getTopLeft().equals(new Coordinate(96,98))) {
				for(Coordinate c : currentBlock.getIngressNodes()) {
					System.out.print(c.toString());
				}
				System.out.println();
			}*/
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
			//return goalBlock.getNode(new Coordinate(map.getWidth()-goalBlock.getTopLeft().getX(),map.getHeight()-goalBlock.getTopLeft().getY()));//return goalBlock.getNode(new Coordinate(blockSize,blockSize));//return goalBlock.getNode(new Coordinate(map.getWidth()-goalBlock.getTopLeft().getX(),map.getHeight()-goalBlock.getTopLeft().getY()));
		
			BASNode goal = goalBlock.getNode(new Coordinate(map.getWidth()-goalBlock.getTopLeft().getX(),map.getHeight()-goalBlock.getTopLeft().getY()));
			//post processing
			//add in intermediate nodes
			BASNode n = goal;
			BASNode nNew;
			while(n != null && n.getParent() !=null) {
				BASNode parent = (BASNode) n.getParent();
				if(n.getBlock() == parent.getBlock()) {
					LinkedList<Coordinate> ll = db.get(n.getBlock().getCode()).get(new PairOfCoords(new Coordinate(n.getX()-n.getBlock().getTopLeft().getX(),n.getY()-n.getBlock().getTopLeft().getY()),new Coordinate(parent.getX()-parent.getBlock().getTopLeft().getX(),parent.getY()-parent.getBlock().getTopLeft().getY()))).getIntermediateNodes();
					for(int i=ll.size()-1;i>=0;i--) {
						Coordinate c = ll.get(i);
						nNew = new BASNode(new Coordinate(c.getX()+n.getBlock().getTopLeft().getX(),c.getY()+n.getBlock().getTopLeft().getY()),n.getBlock());
						n.setParent(nNew);
						n = nNew;
					}
					n.setParent(parent);
				}
				n = parent;
			}
			//remove duplicates
			n = goal;
			while(n != null && n.getParent() !=null) {
				if(!n.getCoordinate().equals(n.getParent().getCoordinate())) {
					n = (BASNode) n.getParent();
				} else {
					while(n.getCoordinate().equals(n.getParent().getCoordinate())) {
						n.setParent(n.getParent().getParent());
					}
					n = (BASNode) n.getParent();
				}
			}
			return (Node) goal;
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
			
			//this next section should only run if we have FINITE WIDTH AGENTS - to avoid diagonal blockages
			/*boolean TL,TR,BL,BR;
			Iterator<Coordinate> listXIt = ListX.iterator();
			Iterator<Coordinate> listXPrimeIt = ListXPrime.iterator();
			while(listXIt.hasNext()){
				Coordinate c = listXIt.next();
				listXPrimeIt.next();
				int cX = c.getX();
				int cY = c.getY();
				try {
					TL = map.getCell(new Coordinate(cX-1+currentBlock.getTopLeft().getX(),cY-1+currentBlock.getTopLeft().getY())).isBlocked();
				} catch (ArrayIndexOutOfBoundsException e) {
					TL = false;
				}
				try {
					TR = map.getCell(new Coordinate(cX+currentBlock.getTopLeft().getX(),cY-1+currentBlock.getTopLeft().getY())).isBlocked();
				} catch (ArrayIndexOutOfBoundsException e) {
					TR = false;
				}
				try {
					BL = map.getCell(new Coordinate(cX-1+currentBlock.getTopLeft().getX(),cY+currentBlock.getTopLeft().getY())).isBlocked();
				} catch (ArrayIndexOutOfBoundsException e) {
					BL = false;
				}
				try {
					BR = map.getCell(new Coordinate(cX+currentBlock.getTopLeft().getX(),cY+currentBlock.getTopLeft().getY())).isBlocked();
				} catch (ArrayIndexOutOfBoundsException e) {
					BR = false;
				}
				if((TL && BR) || (TR && BL)) {
					listXIt.remove();
					listXPrimeIt.remove();
				}
			}*/
			//extra section for finite width ends here
			
			LinkedList<Coordinate> ListXPrimeUpdated = new LinkedList<Coordinate>();
			for(int i=0;i<ListX.size();i++) {
				Coordinate x = ListX.get(i);
				for(Coordinate c : y) {
					double length = db.get(currentBlock.getCode()).get(new PairOfCoords(x,c)) != null ? db.get(currentBlock.getCode()).get(new PairOfCoords(x,c)).getLength() : Double.POSITIVE_INFINITY;
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
	
	private static int getMapCode(Coordinate topLeft) {
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
	
	private static void initStart(Block block) {
		Coordinate startCoord = new Coordinate(0,0);
		block.setHeapValue(getDistance(new Coordinate(0,0),new Coordinate(map.getWidth(),map.getHeight())));	//doesn't actually matter what this is
		int code = getMapCode(startCoord);
		for(int i=0; i<blockSize;i++) {
			Coordinate[] outArray = {new Coordinate(blockSize,i),new Coordinate(blockSize-i,blockSize)};
			for(Coordinate c : outArray) {
				double length = db.get(code).get(new PairOfCoords(startCoord,c)) != null ? db.get(code).get(new PairOfCoords(startCoord,c)).getLength() : Double.POSITIVE_INFINITY;
				block.setGValue(c, length);
				block.setParent(c, block.getNode(startCoord));
			}
		}
	}
	
	private static void initGoal(Block block) {
		Coordinate endCoord = new Coordinate(map.getWidth()-block.getTopLeft().getX(),map.getHeight()-block.getTopLeft().getY());//Coordinate endCoord = new Coordinate(blockSize,blockSize);//Coordinate endCoord = new Coordinate(map.getWidth()-block.getTopLeft().getX(),map.getHeight()-block.getTopLeft().getY());
		int code = getMapCode(block.getTopLeft());
		for(int i=0; i<=blockSize;i++) {
			Coordinate[] outArray = {new Coordinate(0,i),new Coordinate(i,0)};
			for(Coordinate c : outArray) {
				double length = db.get(code).get(new PairOfCoords(c,endCoord)) != null ? db.get(code).get(new PairOfCoords(c,endCoord)).getLength() : Double.POSITIVE_INFINITY;
				block.setHValue(c, length);
			}
		}
	}
	
	private static void initBlockArray(Block[][] blockArray, int blockArrayWidth, int blockArrayHeight) {
		for(int j=0; j<blockArrayHeight;j++) {
			for(int i=0; i<blockArrayWidth;i++) {
				blockArray[i][j] = new Block(getMapCode(new Coordinate(i*blockSize,j*blockSize)),blockSize,new Coordinate(i*blockSize,j*blockSize),new Coordinate(map.getWidth(),map.getHeight()));
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
	
	private static ArrayList<HashMap<PairOfCoords,LengthAndIntermediateNodes>> loadDB() {
		try {
			String filename = "/Users/olly_freeman/Dropbox/Part2Project/"+blockSize+"x"+blockSize+"zerofulldb.ser";
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			ArrayList<HashMap<PairOfCoords,LengthAndIntermediateNodes>> db = (ArrayList<HashMap<PairOfCoords,LengthAndIntermediateNodes>>) in.readObject();
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
