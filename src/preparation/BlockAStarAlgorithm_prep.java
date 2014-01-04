package preparation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import engine.map.Map;
import utility.Coordinate;
import engine.graph.Graph;
import engine.graph.Node;
import engine.graph.PairOfCoords;

public class BlockAStarAlgorithm_prep {

	private static PriorityQueue<Block> openSet = new PriorityQueue<Block>();
	private final static int blockSize = 4;
	private final static LinkedList<HashMap<PairOfCoords,Double>> db = loadDB();
	
	public static Node getPath(Map map) { 
		Block[][] blockArray = new Block[map.getWidth()/blockSize][map.getHeight()/blockSize];
		for(int j=0; j<map.getHeight()/blockSize;j++) {
			for(int i=0; i<map.getWidth()/blockSize;i++) {
				blockArray[i][j] = new Block(getMapCode(new Coordinate(i*blockSize,j*blockSize),map),blockSize,new Coordinate(i*blockSize,j*blockSize),new Coordinate(map.getWidth(),map.getHeight()));
			}
		}
		for(int j=0; j<map.getHeight()/blockSize;j++) {
			for(int i=0; i<map.getWidth()/blockSize;i++) {
				if(j!=0) {
					blockArray[i][j].addNeighbour(blockArray[i][j-1]);
					blockArray[i][j-1].addNeighbour(blockArray[i][j]);
				}
				if(j!=(map.getHeight()/blockSize)-1) {
					blockArray[i][j].addNeighbour(blockArray[i][j+1]);
					blockArray[i][j+1].addNeighbour(blockArray[i][j]);
				}
				if(i!=0) {
					blockArray[i][j].addNeighbour(blockArray[i-1][j]);
					blockArray[i-1][j].addNeighbour(blockArray[i][j]);
				}
				if(i!=(map.getHeight()/blockSize)-1) {
					blockArray[i][j].addNeighbour(blockArray[i+1][j]);
					blockArray[i+1][j].addNeighbour(blockArray[i][j]);
				}
			}
		}
		Block startBlock = blockArray[0][0];
		Block goalBlock = blockArray[(map.getHeight()/blockSize)-1][(map.getHeight()/blockSize)-1];
		initStart(startBlock,map);
		initGoal(goalBlock,map);
		double length = Double.POSITIVE_INFINITY;
		openSet.add(startBlock);
		
		while(!openSet.isEmpty() && openSet.peek().getHeapValue() < length) {
			Block currentBlock = openSet.remove();
			List<Coordinate> ingressNodes = currentBlock.getIngressNodes();
			if(currentBlock == goalBlock) {
				for(Coordinate c : ingressNodes) {
					length = Math.min(length, goalBlock.getGValue(c)+goalBlock.getHValue(c));
				}
			}
			expand(currentBlock,ingressNodes);
		}
		if(length!=Double.POSITIVE_INFINITY) {
			System.out.println("Found path!");
			return traceback(goalBlock);
		} else {
			System.out.println("NO PATH!");
			return null;
		}
	}
	
	private static void expand(Block currentBlock, List<Coordinate> y) {
		for(Block b : currentBlock.getNeighbours()) {
			LinkedList<Coordinate> ListX = new LinkedList<Coordinate>();
			LinkedList<Coordinate> ListXPrime = new LinkedList<Coordinate>();
			Coordinate neighbourTL = b.getTopLeft();
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
					ListX.add(new Coordinate(i,0));
					ListXPrime.add(new Coordinate(i,blockSize));
				}
			} else if(neighbourTL.getY() < currentTL.getY() && neighbourTL.getX() == currentTL.getX()) {
				for(int i=0;i<=blockSize;i++) {
					ListX.add(new Coordinate(i,blockSize));
					ListXPrime.add(new Coordinate(i,0));
				}
			} else {
				System.out.println("Neighbour error!");
			}
			
			LinkedList<Coordinate> ListXPrimeUpdated = new LinkedList<Coordinate>();
			for(int i=0;i<ListX.size();i++) {
				Coordinate x = ListX.get(i);
				for(Coordinate c : y) {
					currentBlock.setGValue(x, Math.min(currentBlock.getGValue(c) + db.get(currentBlock.getCode()).get(new PairOfCoords(x,c)), currentBlock.getGValue(x)));
				}
				Coordinate xPrime = ListXPrime.get(i);
				double xPrimeG = b.getGValue(xPrime);
				double xG = currentBlock.getGValue(x);
				if(xG < xPrimeG) {
					b.setGValue(xPrime,xG);
					ListXPrimeUpdated.add(xPrime);
				}
			}
			double newHeapValue = Double.POSITIVE_INFINITY;
			for(Coordinate c : ListXPrimeUpdated) {
				if(b.getGValue(c)+b.getHValue(c) < newHeapValue) {
					newHeapValue = b.getGValue(c)+b.getHValue(c);
				}
			}
			if(newHeapValue < b.getHeapValue()) {
				b.setHeapValue(newHeapValue);
				if(!openSet.contains(b)) {
					openSet.add(b);
				}
			}
		}
	}
	
	private static Node traceback(Block goalBlock) {
		Node goalNode = new Node(goalBlock.getBestEgress());
		System.out.println(goalNode.coordinateAsString());
		Node n = new Node(goalBlock.getBestIngress());
		System.out.println(n.coordinateAsString());
		goalNode.setParent(n);
		Block currentBlock = goalBlock.getParent();
		while(currentBlock!=null) {
			Node parentNode = new Node(currentBlock.getBestIngress());
			n.setParent(parentNode);
			n = parentNode;
			System.out.println(n.coordinateAsString());
			currentBlock = currentBlock.getParent();
		}
		return goalNode;
	}
	
	private static int getMapCode(Coordinate topLeft, Map map) {
		int code = 0;
		for(int j=topLeft.getY();j<=blockSize;j++) {
			for(int i=topLeft.getX();i<=blockSize;i++) {
				code*=2;
				if(!map.getCell(j, i).isBlocked()) {
					code++;
				}
			}
		}
		return code;
	}
	
	private static void initStart(Block block, Map map) {
		Coordinate startCoord = new Coordinate(0,0);
		block.setBestIngress(startCoord,getDistance(new Coordinate(0,0),new Coordinate(map.getWidth(),map.getHeight())));			//CAN HAVE ANY HEAP VALUE, BECAUSE START BLOCK WILL BE PUSHED THEN IMMEDIATELY POPPED FROM QUEUE?? 
		int code = getMapCode(startCoord,map);
		//double bestEgressValue = Double.POSITIVE_INFINITY;
		//Coordinate bestEgressCoord = null;
		for(int i=0; i<blockSize;i++) {
			Coordinate[] outArray = {new Coordinate(blockSize,i),new Coordinate(blockSize-i,blockSize)};	//start block (top left), therefore only need left and bottom edges
			for(Coordinate c : outArray) {
				block.setGValue(c, db.get(code).get(new PairOfCoords(startCoord,c)));
			}
		}
	}
	
	private static void initGoal(Block block, Map map) {
		//DON'T NEED HEAP VALUE FOR GOAL BLOCK YET
		Coordinate endCoord = new Coordinate(map.getWidth(),map.getHeight());
		block.setBestEgress(endCoord);
		int code = getMapCode(new Coordinate(map.getWidth()-blockSize,map.getHeight()-blockSize),map);
		//Coordinate bestEgressCoord = endCoord;
		//double bestIngressValue = Double.POSITIVE_INFINITY;
		for(int i=0; i<blockSize;i++) {
			Coordinate[] outArray = {new Coordinate(blockSize,i),new Coordinate(blockSize-i,blockSize)};
			for(Coordinate c : outArray) {
				block.setHValue(c, db.get(code).get(new PairOfCoords(c,endCoord)));
			}
		}
	}
	
	private static LinkedList<HashMap<PairOfCoords,Double>> loadDB() {
		try {
			String filename = "/Users/olly_freeman/Dropbox/Part2Project/4by4db.ser";
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			LinkedList<HashMap<PairOfCoords,Double>> db = (LinkedList<HashMap<PairOfCoords,Double>>) in.readObject();
			in.close();
			fileIn.close();
			return db;
			/*for(Entry<PairOfCoords, Double> e : db.get(10).entrySet()) {
				System.out.println("Loading: " + e.getKey().toString() + ", " + e.getValue());
			}*/
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
