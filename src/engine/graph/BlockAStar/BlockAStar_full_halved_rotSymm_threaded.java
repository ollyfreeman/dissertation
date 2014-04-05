package engine.graph.BlockAStar;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import utility.Coordinate;
import utility.Pair;
import engine.AlgorithmData;
import engine.graph.Graph;
import engine.graph.Node;
import engine.graph.BlockAStar.LDDB.PairOfCoords;
import engine.map.Map;
import engine.graph.BlockAStar.LDDB.compressedAsLong.rotSymm.LDDB;
import engine.graph.BlockAStar.LDDB.compressedAsLong.rotSymm.BASNode;
import engine.graph.BlockAStar.LDDB.compressedAsLong.rotSymm.Block;

public class BlockAStar_full_halved_rotSymm_threaded extends AlgorithmData {

	protected static final long serialVersionUID = 1L;

	protected static final int blockSize = 3;
	protected static LDDB lddb;

	protected Coordinate startInBlock,goalInBlock;
	
	protected int nodesExpanded = 0;
	protected int blockExpanded = 0;
	protected Block[][] blockArray;
	
	private HashSet<Coordinate> expanded = new HashSet<Coordinate>();
	
	//FOR THREADED IMPL
	Block currentBlock;
	//Block neighbourBlock;
	LinkedList<Coordinate> y;
	LinkedList<Coordinate> rotatedY;
	PriorityQueue<Block> openSet;
	ArrayList<Block> neighbourBlocks;

	public BlockAStar_full_halved_rotSymm_threaded(Map map, Coordinate start, Coordinate goal) {
		super();
			this.source = start;
			this.goal = goal;
			double startTime = System.nanoTime();
			blockArray = initBlockArray(map);
			double endTime = System.nanoTime();
			this.graphCreationTime = (endTime - startTime)/1000000;
	}

	@Override
	public Pair<Node, int[][]> getPath(Graph graph, Map map, int[][] nea) {
		//double startTime = System.nanoTime();
		openSet = new PriorityQueue<Block>();

		//initialise
		
		Block startBlock = initStart(blockArray,map);
		Block goalBlock = initGoal(blockArray,map);
		//double stopTime = System.nanoTime();
		//System.out.println("Init time: = " + ((stopTime-startTime)/1000000));
		if(startBlock == goalBlock) {
			return new Pair<Node,int[][]>(startAndGoalInSameBlock(startBlock,goalBlock,map),nea);
		}
		
		//startTime = System.nanoTime();
		double length = Double.POSITIVE_INFINITY;
		openSet.add(startBlock);

		while(!openSet.isEmpty() && openSet.peek().getHeapValue() < length) {
			currentBlock = openSet.remove();
			blockExpanded++;
			currentBlock.setHeapValue(Double.POSITIVE_INFINITY);					//!!
			LinkedList<Coordinate> ingressNodes = currentBlock.getIngressNodes();
			nodesExpanded+=ingressNodes.size();
			for(int i=0;i<ingressNodes.size();i++) {
				//expanded.add(new Coordinate(currentBlock.getTopLeft().getX() + ingressNodes.get(i).getX(), currentBlock.getTopLeft().getY() + ingressNodes.get(i).getY()));
				nea[currentBlock.getTopLeft().getX() + ingressNodes.get(i).getX()][currentBlock.getTopLeft().getY() + ingressNodes.get(i).getY()]++;
			}
			if(currentBlock == goalBlock) {
				for(Coordinate c : ingressNodes) {
					if(goalBlock.getGValue(c)+goalBlock.getHValue(c) < length) {
						length = goalBlock.getGValue(c)+goalBlock.getHValue(c);
					}
				}
			}
			expand(ingressNodes, openSet);
		}
		if(length!=Double.POSITIVE_INFINITY) {
			//stopTime = System.nanoTime();
			//System.out.println("Main time: = " +((stopTime-startTime)/1000000));
			System.out.println("Node expansions: " + nodesExpanded + ", nodes expanded:" +  expanded.size() + ", block expansions: " + blockExpanded);
			//System.out.println("Length: " + length);
			return new Pair<Node,int[][]>(postProcessing(startBlock,goalBlock,map),nea);
		} else {
			//System.out.println("Length: " + length);
			return new Pair<Node,int[][]>(null,nea);
		}
	}

	protected void expand(LinkedList<Coordinate> ingressNodes, PriorityQueue<Block> openSet) {
		y=ingressNodes;
		rotatedY = new LinkedList<Coordinate>();
		for(int i=0;i<y.size();i++) {
			rotatedY.add(i, currentBlock.adjustCoordinates(y.get(i), true));
		}
		neighbourBlocks = currentBlock.getNeighbours();
		
		Thread thread = new Thread(){
			public void run(){
				for(int i=0;i<neighbourBlocks.size()/2;i++) {
					expandBody(neighbourBlocks.get(i));
				}  
			}
		};
		thread.start();
		for(int i=neighbourBlocks.size()/2;i<neighbourBlocks.size();i++) {
			expandBody(neighbourBlocks.get(i));
		}
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("thread error");
		}
		
		

	}
	
	protected void expandBody(Block neighbourBlock) {
		LinkedList<Coordinate> ListX = new LinkedList<Coordinate>();		//egress cells in currentBlock for this neighbourBlock
		LinkedList<Coordinate> ListXPrime = new LinkedList<Coordinate>();	//corresponding ingress cell in the neighbourBlock
		LinkedList<Coordinate> rotatedX = new LinkedList<Coordinate>();
		Coordinate neighbourTL = neighbourBlock.getTopLeft();				//i.e. the topLeft coordinate of the neighbour
		Coordinate currentTL = currentBlock.getTopLeft();

		if(neighbourTL.getY() < currentTL.getY() && neighbourTL.getX() < currentTL.getX()) {
			ListX.add(new Coordinate(0,0));
			ListXPrime.add(new Coordinate(blockSize,blockSize));
			rotatedX = currentBlock.getRotatedEgress(0);
		} else if(neighbourTL.getY() < currentTL.getY() && neighbourTL.getX() == currentTL.getX()) {
			for(int i=1;i<blockSize;i++) {
				ListX.add(new Coordinate(i,0));
				ListXPrime.add(new Coordinate(i,blockSize));
			}
			rotatedX = currentBlock.getRotatedEgress(1);
		} else if(neighbourTL.getY() < currentTL.getY() && neighbourTL.getX() > currentTL.getX()) {
			ListX.add(new Coordinate(blockSize,0));
			ListXPrime.add(new Coordinate(0,blockSize));
			rotatedX = currentBlock.getRotatedEgress(2);
		} else if(neighbourTL.getX() > currentTL.getX() && neighbourTL.getY() == currentTL.getY()) {
			for(int i=1;i<blockSize;i++) {
				ListX.add(new Coordinate(blockSize,i));
				ListXPrime.add(new Coordinate(0,i));
			}
			rotatedX = currentBlock.getRotatedEgress(3);
		} else if(neighbourTL.getY() > currentTL.getY() && neighbourTL.getX() > currentTL.getX()) {
			ListX.add(new Coordinate(blockSize,blockSize));
			ListXPrime.add(new Coordinate(0,0));
			rotatedX = currentBlock.getRotatedEgress(4);
		} else if(neighbourTL.getY() > currentTL.getY() && neighbourTL.getX() == currentTL.getX()) {
			for(int i=1;i<blockSize;i++) {
				ListX.add(new Coordinate(i,blockSize));
				ListXPrime.add(new Coordinate(i,0));
			}
			rotatedX = currentBlock.getRotatedEgress(5); 
		} else if(neighbourTL.getY() > currentTL.getY() && neighbourTL.getX() < currentTL.getX()) {
			ListX.add(new Coordinate(0,blockSize));
			ListXPrime.add(new Coordinate(blockSize,0));
			rotatedX = currentBlock.getRotatedEgress(6);
		} else if(neighbourTL.getX() < currentTL.getX() && neighbourTL.getY() == currentTL.getY()) {
			for(int i=1;i<blockSize;i++) {
				ListX.add(new Coordinate(0,i));
				ListXPrime.add(new Coordinate(blockSize,i));
			}
			rotatedX = currentBlock.getRotatedEgress(7);
		}else {
			System.out.println("Neighbour error!");
		}

		LinkedList<Coordinate> ListXPrimeUpdated = new LinkedList<Coordinate>();
		for(int i=0;i<ListX.size();i++) {
			Coordinate x = ListX.get(i);
			Coordinate out = rotatedX.get(i);
			for(int j=0;j<y.size();j++) {
				double length;
				Coordinate in = rotatedY.get(j);
				if((in.getX()<=out.getX() && in.getY()==out.getY()) || (out.getY()>in.getY())) {
					length = lddb.getLength(currentBlock.getCode(),new PairOfCoords(in,out,blockSize));
				} else {
					length = lddb.getLength(currentBlock.getCode(),new PairOfCoords(out,in,blockSize));
				}
				if(currentBlock.getGValue(y.get(j)) + length < currentBlock.getGValue(x)) {
					currentBlock.setGValue(x, currentBlock.getGValue(y.get(j)) + length);
					//parent of x is c
					currentBlock.setParent(x, currentBlock.getNode(y.get(j)));
				}
			}
			//System.out.println();
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
			if(openSet.contains(neighbourBlock)) {
				openSet.remove(neighbourBlock);
			}
			openSet.add(neighbourBlock);
		}
	}

	protected Block[][] initBlockArray(Map map) {
		int blockArrayWidth = (int) Math.ceil((map.getWidth()+0.0)/blockSize);
		int blockArrayHeight = (int) Math.ceil((map.getHeight()+0.0)/blockSize);
		Block[][] blockArray = new Block[blockArrayWidth][blockArrayHeight];
		for(int j=0; j<blockArrayHeight;j++) {
			for(int i=0; i<blockArrayWidth;i++) {
				blockArray[i][j] = new Block(map,new Coordinate(i*(blockSize),j*(blockSize)),blockSize,this.goal);
			}
		}
		Coordinate[] neighbourBlocks = {new Coordinate(-1,-1),new Coordinate(0,-1),new Coordinate(1,-1),new Coordinate(1,0),new Coordinate(1,1),new Coordinate(0,1),new Coordinate(-1,1),new Coordinate(-1,0)};

		for(int j=0; j<blockArrayHeight;j++) {
			for(int i=0; i<blockArrayWidth;i++) {
				for(Coordinate c : neighbourBlocks) {
					try {
						blockArray[i][j].addNeighbour(blockArray[i+c.getX()][j+c.getY()]);
					} catch (ArrayIndexOutOfBoundsException e) {}
				}
			}
		}
		return blockArray;
	}

	protected Block initStart(Block[][] blockArray, Map map) {
		Block startBlock = blockArray[this.source.getX()/blockSize][this.source.getY()/blockSize];
		startInBlock = new Coordinate(this.source.getX()%blockSize,this.source.getY()%blockSize);
		startBlock.setHeapValue(0);        //doesn't actually matter what this is
		for(int i=0; i<blockSize;i++) {
			Coordinate[] outArray ={new Coordinate(i,0),new Coordinate(blockSize,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};//Coordinate[] outArray = {new Coordinate(i,0),new Coordinate(0,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};
			for(Coordinate c : outArray) {
				float length;
				ArrayList<Coordinate> intermediateNodes;
				int j,jLimit,jStep;
				Coordinate in = startBlock.adjustCoordinates(startInBlock,true);
				Coordinate out = startBlock.adjustCoordinates(c,true);
				if((in.getX()<=out.getX() && in.getY()==out.getY()) || (out.getY()>in.getY())) {
					length = lddb.getLength(startBlock.getCode(),(new PairOfCoords(in,out,blockSize)));
					intermediateNodes = lddb.getIntermediateNodes(startBlock.getCode(),(new PairOfCoords(in,out,blockSize)));
					j = intermediateNodes.size()-1;
					jLimit = -1;
					jStep = -1;
				} else {
					length = lddb.getLength(startBlock.getCode(),(new PairOfCoords(out,in,blockSize)));
					intermediateNodes = lddb.getIntermediateNodes(startBlock.getCode(),(new PairOfCoords(out,in,blockSize)));
					j = 0;
					jLimit = intermediateNodes.size();
					jStep = 1;
				}
				startBlock.setGValue(c, length);
				if(!c.equals(startInBlock)) {
					Node n = startBlock.getNode(c);
					for(;j!=jLimit;j+=jStep) {
						Coordinate c1 = startBlock.adjustCoordinates(intermediateNodes.get(j),false);
						n.setParent(startBlock.getNode(c1));
						n = startBlock.getNode(c1);
					}
					n.setParent(startBlock.getNode(startInBlock));
				}
			}
		}
		return startBlock;
	}

	protected Block initGoal(Block[][] blockArray, Map map) {
		Block goalBlock;
		int x,y;
		if(this.goal.equals(new Coordinate(0,0))) {
			goalBlock = blockArray[0][0];	//-1 to make it 1 block closer to start if on an edge - to stop index out of bounds if in bottom right
			x=0;
			y=0;
		} else if (this.goal.equals(new Coordinate(map.getWidth(),map.getHeight()))){
			goalBlock = blockArray[blockArray.length-1][blockArray[0].length-1];
			x = this.goal.getX()%blockSize != 0 ? this.goal.getX()%blockSize : blockSize;
			y = this.goal.getY()%blockSize != 0 ? this.goal.getY()%blockSize : blockSize;
		} else {
			goalBlock = blockArray[(this.goal.getX())/blockSize][(this.goal.getY())/blockSize];
			x = this.goal.getX()%blockSize;
			y = this.goal.getY()%blockSize;
		}
		goalInBlock = new Coordinate(x,y);
		for(int i=0; i<blockSize;i++) {
			Coordinate[] outArray ={new Coordinate(i,0),new Coordinate(blockSize,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};//Coordinate[] outArray = {new Coordinate(i,0),new Coordinate(0,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};
			for(Coordinate c : outArray) {
				double length;
				Coordinate in = goalBlock.adjustCoordinates(c,true);
				Coordinate out = goalBlock.adjustCoordinates(goalInBlock,true);
				if((in.getX()<=out.getX() && in.getY()==out.getY()) || (out.getY()>in.getY())) {
					length = lddb.getLength(goalBlock.getCode(),new PairOfCoords(in,out,blockSize));
				} else {
					length = lddb.getLength(goalBlock.getCode(),new PairOfCoords(out,in,blockSize));
				}
				goalBlock.setHValue(c, length);
			}
		}
		return goalBlock;
	}

	protected Node startAndGoalInSameBlock(Block startBlock, Block goalBlock, Map map) {
		float length;
		ArrayList<Coordinate> intermediateNodes;
		int j,jLimit,jStep;
		Coordinate in = goalBlock.adjustCoordinates(startInBlock,true);
		Coordinate out = goalBlock.adjustCoordinates(goalInBlock,true);
		if((in.getX()<=out.getX() && in.getY()==out.getY()) || (out.getY()>in.getY())) {
			length = lddb.getLength(goalBlock.getCode(),(new PairOfCoords(in,out,blockSize)));
			intermediateNodes = lddb.getIntermediateNodes(goalBlock.getCode(),(new PairOfCoords(in,out,blockSize)));
			j = intermediateNodes.size()-1;
			jLimit = -1;
			jStep = -1;
		} else {
			length = lddb.getLength(goalBlock.getCode(),(new PairOfCoords(out,in,blockSize)));
			intermediateNodes = lddb.getIntermediateNodes(goalBlock.getCode(),(new PairOfCoords(out,in,blockSize)));
			j = 0;
			jLimit = intermediateNodes.size();
			jStep = 1;
		}
		if(length != Float.POSITIVE_INFINITY) {
			Node n = goalBlock.getNode(goalInBlock);
			if(!startInBlock.equals(goalInBlock)) {
				for(;j!=jLimit;j+=jStep) {
					Coordinate c1 = goalBlock.adjustCoordinates(intermediateNodes.get(j),false);
					n.setParent(goalBlock.getNode(c1));
					n = goalBlock.getNode(c1);
				}
				n.setParent(goalBlock.getNode(startInBlock));
			}
			return goalBlock.getNode(goalInBlock);
		} else {
			return null;
		}
	}

	protected int getMapCode(Coordinate topLeft, Map map) {
		int code = 0;
		for(int j=0;j<blockSize;j++) {
			for(int i=0;i<blockSize;i++) {
				code = code<<1;
				try{
					if(!map.getCell(i+topLeft.getX(),j+topLeft.getY()).isBlocked()) {
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

	protected Node postProcessing(Block startBlock, Block goalBlock, Map map) {
		BASNode goal = goalBlock.getNode(goalInBlock);
		//post processing
		//add in intermediate nodes
		BASNode n = goal;

		//goalBlock
		Coordinate minGplusHCoordinate = null;
		double minGPlusH=Double.POSITIVE_INFINITY;
		for(int i=0; i<blockSize;i++) {
			Coordinate[] outArray ={new Coordinate(i,0),new Coordinate(blockSize,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};//Coordinate[] outArray = {new Coordinate(i,0),new Coordinate(0,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};
			for(Coordinate c : outArray) {
				if(goalBlock.getNode(c).getF()+goalBlock.getNode(c).getG()<minGPlusH) {
					minGplusHCoordinate = c;
					minGPlusH = goalBlock.getNode(c).getF()+goalBlock.getNode(c).getG();
				}
			}
		}
		List<Coordinate> intermediateNodes;	
		int j, jLimit, jStep;
		Coordinate in = goalBlock.adjustCoordinates(minGplusHCoordinate,true);
		Coordinate out = goalBlock.adjustCoordinates(goalInBlock,true);
		if((in.getX()<=out.getX() && in.getY()==out.getY()) || (out.getY()>in.getY())) {
			intermediateNodes = lddb.getIntermediateNodes(goalBlock.getCode(),(new PairOfCoords(in,out,blockSize)));
			j = intermediateNodes.size()-1;
			jLimit = -1;
			jStep = -1;
		} else {
			intermediateNodes = lddb.getIntermediateNodes(goalBlock.getCode(),(new PairOfCoords(out,in,blockSize)));
			j = 0;
			jLimit = intermediateNodes.size();
			jStep = 1;
		}
		if(!minGplusHCoordinate.equals(goalInBlock)) {
			for(;j!=jLimit;j+=jStep) {
				Coordinate c1 = goalBlock.adjustCoordinates(intermediateNodes.get(j),false);
				n.setParent(goalBlock.getNode(c1));
				n = goalBlock.getNode(c1);
			}
			n.setParent(goalBlock.getNode(minGplusHCoordinate));
			n = (BASNode) goalBlock.getNode(minGplusHCoordinate).getParent();
		}
		BASNode nNew;
		while(n != null && n.getParent() != null) {
			BASNode parent = (BASNode) n.getParent();
			if(n.getBlock() == parent.getBlock() && !n.getBlock().equals(startBlock)) {
				in = n.getBlock().adjustCoordinates(new Coordinate(n.getX()-n.getBlock().getTopLeft().getX(),n.getY()-n.getBlock().getTopLeft().getY()), true);
				out = n.getBlock().adjustCoordinates(new Coordinate(parent.getX()-parent.getBlock().getTopLeft().getX(),parent.getY()-parent.getBlock().getTopLeft().getY()),true);
				if((in.getX()<=out.getX() && in.getY()==out.getY()) || (out.getY()>in.getY())) {
					intermediateNodes = lddb.getIntermediateNodes(n.getBlock().getCode(),(new PairOfCoords(in,out,blockSize)));
					j = intermediateNodes.size()-1;
					jLimit = -1;
					jStep = -1;
				} else {
					intermediateNodes = lddb.getIntermediateNodes(n.getBlock().getCode(),(new PairOfCoords(out,in,blockSize)));
					j = 0;
					jLimit = intermediateNodes.size();
					jStep = 1;
				}
				for(;j!=jLimit;j+=jStep) {
					Coordinate c = n.getBlock().adjustCoordinates(intermediateNodes.get(j),false);
					nNew = new BASNode(new Coordinate(c.getX()+n.getBlock().getTopLeft().getX(),c.getY()+n.getBlock().getTopLeft().getY()),n.getBlock());
					n.setParent(nNew);
					n = nNew;
				}
				n.setParent(parent);
				n = parent;
			} else {
				n = parent;
			}
		}
		//remove duplicates
		n = goal;
		while(n != null && n.getParent() !=null) {
			if(!n.getCoordinate().equals(n.getParent().getCoordinate())) {
				n = (BASNode) n.getParent();
			} else {
				while(n.getParent() !=null && n.getCoordinate().equals(n.getParent().getCoordinate())) {
					n.setParent(n.getParent().getParent());
				}
				if(n.getParent() !=null) {
					n = (BASNode) n.getParent();
				}
			}
		}
		return (Node) goal;
	}

	public static void loadDB(String dbType) {
		try {
			if(lddb == null) {
				String filename = "/Users/olly_freeman/Dropbox/Part2Project/"+blockSize+"zero_"+dbType+".ser";
				FileInputStream fileIn = new FileInputStream(filename);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				LDDB db = (LDDB) in.readObject();
				in.close();
				fileIn.close();
				lddb = db;
			}
		} catch(IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		}
	}

}
