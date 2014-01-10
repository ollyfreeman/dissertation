package engine.graph.BlockAStar;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import utility.Coordinate;
import utility.Pair;
import engine.AlgorithmData;
import engine.graph.Graph;
import engine.graph.GraphGenerator;
import engine.graph.Node;
import engine.graph.AStar.AStar;
import engine.graph.AStar.AStarAlgorithm;
import engine.graph.BlockAStar.LDDB.LDDB;
import engine.graph.BlockAStar.LDDB.PairOfCoords;
import engine.map.Map;

public class BlockAStar extends AlgorithmData {

	private static final long serialVersionUID = 1L;
	
	private static final int blockSize = 3;
	private static LDDB lddb = loadDB();

	private Coordinate startInBlock,goalInBlock;
	private int nodesExpanded = 0;

	public BlockAStar() {
		super();
	}

	@Override
	public Pair<Node, Integer> getPath(Graph graph, Map map) {
		PriorityQueue<Block> openSet = new PriorityQueue<Block>();
		Block[][] blockArray = initBlockArray(map);
		Block startBlock = initStart(graph.getSource().getCoordinate(),blockArray,map);
		Block goalBlock = initGoal(graph.getGoal().getCoordinate(),blockArray,map);
		
		if(startBlock == goalBlock) {
			return new Pair<Node,Integer>(startAndGoalInSameBlock(startBlock,goalBlock,map),nodesExpanded);
		}
		
		double length = Double.POSITIVE_INFINITY;
		openSet.add(startBlock);
		
		while(!openSet.isEmpty() && openSet.peek().getHeapValue() < length) {
			Block currentBlock = openSet.remove();
			currentBlock.setHeapValue(Double.POSITIVE_INFINITY);					//!!
			List<Coordinate> ingressNodes = currentBlock.getIngressNodes();
			nodesExpanded+=ingressNodes.size();
			if(currentBlock == goalBlock) {
				for(Coordinate c : ingressNodes) {
					if(goalBlock.getGValue(c)+goalBlock.getHValue(c) < length) {
						length = goalBlock.getGValue(c)+goalBlock.getHValue(c);
						//parent of goal is c
						//goalBlock.setParent(goalInBlock, goalBlock.getNode(c));
					}
				}
			}
			expand(currentBlock,ingressNodes, openSet);
		}
		if(length!=Double.POSITIVE_INFINITY) {
			return new Pair<Node,Integer>(postProcessing(startBlock,goalBlock,map),nodesExpanded);
		} else {
			System.out.println("NO PATH!");
			return new Pair<Node,Integer>(null,nodesExpanded);
		}
	}
	
	private void expand(Block currentBlock, List<Coordinate> y, PriorityQueue<Block> openSet) {
		for(Block neighbourBlock : currentBlock.getNeighbours()) {
			LinkedList<Coordinate> ListX = new LinkedList<Coordinate>();		//egress cells in currentBlock for this neighbourBlock
			LinkedList<Coordinate> ListXPrime = new LinkedList<Coordinate>();	//corresponding ingress cell in the neighbourBlock
			Coordinate neighbourTL = neighbourBlock.getTopLeft();				//i.e. the topLeft coordinate of the neighbour
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
					double length = lddb.getLength(currentBlock.getCode(),new PairOfCoords(x,c,blockSize));
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
	
	private Block[][] initBlockArray(Map map) {
		int blockArrayWidth = (int) Math.ceil((map.getWidth()+0.0)/blockSize);
		int blockArrayHeight = (int) Math.ceil((map.getHeight()+0.0)/blockSize);
		Block[][] blockArray = new Block[blockArrayWidth][blockArrayHeight];
		for(int j=0; j<blockArrayHeight;j++) {
			for(int i=0; i<blockArrayWidth;i++) {
				blockArray[i][j] = new Block(getMapCode(new Coordinate(i*blockSize,j*blockSize),map),blockSize,new Coordinate(i*blockSize,j*blockSize),new Coordinate(map.getWidth(),map.getHeight()));
			}
		}
		Coordinate[] neighbourBlocks = {new Coordinate(0,-1),new Coordinate(1,0),new Coordinate(0,1),new Coordinate(-1,0),};//for FINITE WIDTH - I think
		
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
	
	private Block initStart(Coordinate startCoord,Block[][] blockArray, Map map) {
		Block startBlock = blockArray[startCoord.getX()/blockSize][startCoord.getY()/blockSize];
		startInBlock = new Coordinate(startCoord.getX()%blockSize,startCoord.getY()%blockSize);
		startBlock.setHeapValue(getDistance(startCoord,new Coordinate(map.getWidth(),map.getHeight())));        //doesn't actually matter what this is
		Map m = new Map(map,startBlock.getTopLeft(),blockSize,blockSize);
        for(int i=0; i<blockSize;i++) {
        	Coordinate[] outArray ={new Coordinate(i,0),new Coordinate(blockSize,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};//Coordinate[] outArray = {new Coordinate(i,0),new Coordinate(0,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};
			for(Coordinate c : outArray) {
				Graph g = GraphGenerator.generateGraph_visibility_edge_zeroWidth(m, new Node(startInBlock), new Node(c));
				AStar aStar = new AStar();	aStar.go(g,map);
				double length = aStar.getGoal() != null ? aStar.getDistance() : Double.POSITIVE_INFINITY;
				LinkedList<Coordinate> intermediateNodes = aStar.getPath();
				try {
					intermediateNodes.remove(0); intermediateNodes.remove(intermediateNodes.size()-1);
				} catch (IndexOutOfBoundsException e) {}
				nodesExpanded+=aStar.getNodesExpanded();
				startBlock.setGValue(c, length);
				if(!c.equals(startInBlock)) {
					Node n = startBlock.getNode(c);
					for(Coordinate c1 : intermediateNodes) {
						n.setParent(startBlock.getNode(c1));
						n = startBlock.getNode(c1);
					}
					n.setParent(startBlock.getNode(startInBlock));
				}
			}
		}
		return startBlock;
	}
	
	private Block initGoal(Coordinate goalCoord,Block[][] blockArray, Map map) {
		Block goalBlock;
		int x,y;
		if(goalCoord.equals(new Coordinate(0,0))) {
			goalBlock = blockArray[0][0];	//-1 to make it 1 block closer to start if on an edge - to stop index out of bounds if in bottom right
			x=0;
			y=0;
		} else if (goalCoord.equals(new Coordinate(map.getWidth(),map.getHeight()))){
			goalBlock = blockArray[blockArray.length-1][blockArray[0].length-1];
			x = goalCoord.getX()%blockSize != 0 ? goalCoord.getX()%blockSize : blockSize;
			y = goalCoord.getY()%blockSize != 0 ? goalCoord.getY()%blockSize : blockSize;
		} else {
			goalBlock = blockArray[(goalCoord.getX())/blockSize][(goalCoord.getY())/blockSize];
			x = goalCoord.getX()%blockSize;
			y = goalCoord.getY()%blockSize;
		}
		goalInBlock = new Coordinate(x,y);
		Map m = new Map(map,goalBlock.getTopLeft(),blockSize,blockSize);
        for(int i=0; i<blockSize;i++) {
        	Coordinate[] outArray ={new Coordinate(i,0),new Coordinate(blockSize,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};//Coordinate[] outArray = {new Coordinate(i,0),new Coordinate(0,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};
			for(Coordinate c : outArray) {
				Graph g = GraphGenerator.generateGraph_visibility_edge_zeroWidth(m, new Node(c), new Node(goalInBlock));
				AStar aStar = new AStar();	aStar.go(g,map);
				double length = aStar.getGoal() != null ? aStar.getDistance() : Double.POSITIVE_INFINITY;
				LinkedList<Coordinate> intermediateNodes = aStar.getPath();
				try {
					intermediateNodes.remove(0); intermediateNodes.remove(intermediateNodes.size()-1);
				} catch (IndexOutOfBoundsException e) {}
				nodesExpanded+=aStar.getNodesExpanded();
				goalBlock.setHValue(c, length);
				if(!c.equals(goalInBlock)) {
					Node n = goalBlock.getNode(goalInBlock);
					for(Coordinate c1 : intermediateNodes) {
						n.setParent(goalBlock.getNode(c1));
						n = goalBlock.getNode(c1);
					}
					n.setParent(goalBlock.getNode(c));//HAD ---> before but surely wrong?! goalBlock.getNode(c).setParent(n);
				}
			}
		}
		return goalBlock;
	}
	
	private Node startAndGoalInSameBlock(Block startBlock, Block goalBlock, Map map) {
		Map m = new Map(map,goalBlock.getTopLeft(),blockSize,blockSize);
		
		Graph g = GraphGenerator.generateGraph_visibility_edge_zeroWidth(m, new Node(startInBlock), new Node(goalInBlock));
		AStar aStar = new AStar();	aStar.go(g,map);
		LinkedList<Coordinate> intermediateNodes = aStar.getPath();
		try {
			intermediateNodes.remove(0); intermediateNodes.remove(intermediateNodes.size()-1);
		} catch (IndexOutOfBoundsException e) {}
		nodesExpanded+=aStar.getNodesExpanded();
		Node n = goalBlock.getNode(goalInBlock);
		if(!startInBlock.equals(goalInBlock)) {
			for(Coordinate c1 : intermediateNodes) {
				n.setParent(goalBlock.getNode(c1));
				n = goalBlock.getNode(c1);
			}
			n.setParent(goalBlock.getNode(startInBlock));
		}
		return goalBlock.getNode(goalInBlock);
	}
	
	private int getMapCode(Coordinate topLeft, Map map) {
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
	
	private Node postProcessing(Block startBlock, Block goalBlock, Map map) {
		BASNode goal = goalBlock.getNode(goalInBlock);
		//post processing
		//add in intermediate nodes
		BASNode n = goal;
		
		//goalBlock
		Map m = new Map(map,goalBlock.getTopLeft(),blockSize,blockSize);
		Coordinate minGplusHCoordinate = new Coordinate(0,0);
		double minGPlusH=Double.POSITIVE_INFINITY;//double minGPlusH = goalBlock.getNode(minGplusHCoordinate).getG()+goalBlock.getNode(minGplusHCoordinate).getF();
        for(int i=0; i<blockSize;i++) {
        	Coordinate[] outArray ={new Coordinate(i,0),new Coordinate(blockSize,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};//Coordinate[] outArray = {new Coordinate(i,0),new Coordinate(0,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};
			for(Coordinate c : outArray) {
				if(goalBlock.getNode(c).getF()+goalBlock.getNode(c).getG()<minGPlusH) {
					minGplusHCoordinate = c;
					minGPlusH = goalBlock.getNode(c).getF()+goalBlock.getNode(c).getG();
				}
			}
        }
        Graph g = GraphGenerator.generateGraph_visibility_edge_zeroWidth(m, new Node(minGplusHCoordinate),new Node(goalInBlock)); //generateGraph_visibility_edge_zeroWidth & generateBlockAStarGraph_visibility_edge_zeroWidth are equiv now - so I think I can delete generateBlockAStarGraph...
		Pair<Double,ArrayList<Coordinate>> lAIN = AStarAlgorithm.getLengthAndIntermediateNodes(g,map);
		ArrayList<Coordinate> intermediateNodes = lAIN.get1();
		if(!minGplusHCoordinate.equals(goalInBlock)) {
			for(Coordinate c1 : intermediateNodes) {
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
				intermediateNodes = lddb.getIntermediateNodes(n.getBlock().getCode(),(new PairOfCoords(new Coordinate(n.getX()-n.getBlock().getTopLeft().getX(),n.getY()-n.getBlock().getTopLeft().getY()),new Coordinate(parent.getX()-parent.getBlock().getTopLeft().getX(),parent.getY()-parent.getBlock().getTopLeft().getY()),blockSize)));
				for(int i=(intermediateNodes.size()-1);i>=0;i--) {
					Coordinate c = intermediateNodes.get(i);
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
	
	protected double getDistance(Coordinate c1, Coordinate c2) {
		double xDiff = c1.getX() - c2.getX();
		double yDiff = c1.getY() - c2.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}
	
	private static LDDB loadDB() {
		try {
			String filename = "/Users/olly_freeman/Dropbox/Part2Project/"+blockSize+"zero.ser";
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			System.out.print("Loading DB...");
			LDDB lddb = (LDDB) in.readObject();
			System.out.println("Done");
			in.close();
			fileIn.close();
			return lddb;
		} catch(IOException i) {
			i.printStackTrace();
			return null;
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			return null;
		}

	}

}
