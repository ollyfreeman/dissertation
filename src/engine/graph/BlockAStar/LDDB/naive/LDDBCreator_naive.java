package engine.graph.BlockAStar.LDDB.naive;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import engine.map.Map;
import engine.graph.Graph;
import engine.graph.Node;
import engine.graph.GraphGenerator;
import engine.graph.Dijkstra.DijkstraAlgorithm;
import engine.graph.BlockAStar.LDDB.naive.PairOfCoords_naive;
import utility.Coordinate;
import utility.Pair;

/*
 * Most naive implementation of the LDDB, where I use an ArrayList of HashMaps of unencoded types:
 * PairOfCoords_naive : i.e. where the PairOfCoords HAS 2 Coordinates
 * Double
 * ArrayList<Coordinate>
 */

public class LDDBCreator_naive {

	private static int blockSize = 4;
	
	public static void createDB() {
		int totalMaps = (int) Math.pow(2,blockSize*blockSize);
		ArrayList<HashMap<PairOfCoords_naive,Pair<Double,ArrayList<Coordinate>>>> db = new ArrayList<HashMap<PairOfCoords_naive,Pair<Double,ArrayList<Coordinate>>>>();
		for(int mapCounter=0;mapCounter<totalMaps;mapCounter++) {
			int mapRep = mapCounter;
			int[][] map = new int[blockSize][blockSize];
			for(int j=blockSize-1;j>=0;j--) {
				for(int i=blockSize-1;i>=0;i--) {
					if((mapRep & 1) == 1) {
						map[i][j] = 1;			//1 in all reps means unblocked
					} else {
						map[i][j] = 0; 			//0 in all reps means blocked
					}
					mapRep = mapRep>>>1;
				}
			}
			Map m = new Map(map);
			HashMap<PairOfCoords_naive,Pair<Double,ArrayList<Coordinate>>> hm = new HashMap<PairOfCoords_naive,Pair<Double,ArrayList<Coordinate>>>();
			if(mapCounter==8) {
				m.print();
			}
			for(int i=0; i<blockSize;i++) {
				for(int j=0;j<blockSize;j++) {
					Coordinate[] sourceArray = {new Coordinate(i,0),new Coordinate(blockSize,i),new Coordinate(blockSize-i,blockSize),new Coordinate(0,blockSize-i)};
					Coordinate[] goalArray = {new Coordinate(j,0),new Coordinate(blockSize,j),new Coordinate(blockSize-j,blockSize),new Coordinate(0,blockSize-j)};
					for(Coordinate sourceCoord : sourceArray) {
						for(Coordinate goalCoord : goalArray) {
							f(hm,m,sourceCoord,goalCoord,blockSize,mapCounter);
						}
					}
				}
			}
			db.add(hm);
			System.out.println(mapCounter + " of " + totalMaps);
		}
		saveDB(db);
		//loadDB();
	}

	//mapCounter parameter only for debugging
	private static void f(HashMap<PairOfCoords_naive,Pair<Double,ArrayList<Coordinate>>> hm, Map m, Coordinate sourceCoord, Coordinate goalCoord, int size, int mapCounter) {
		Node sourceNode = new Node(sourceCoord);
		Node goalNode = new Node(goalCoord);
		Graph g = GraphGenerator.generateGraph_visibility_edge_zeroWidth(m, sourceNode, goalNode); //this was working with GraphGenerator.generateBlockAStarGraph_visibility_edge_zeroWidth

		double distanceAccumulator = 0.0;
		ArrayList<Coordinate> intermediateCoordinates = new ArrayList<Coordinate>();
		g.setSource(g.getNode(sourceCoord));
		g.setGoal(g.getNode(goalCoord));
		Node n = DijkstraAlgorithm.getPath(g);
		if(n!=null) {
			while(n != null) {
				try {
					distanceAccumulator+=getDistance(n,n.getParent());
				} catch (NullPointerException e) {
					//when we get to the final (source) node
				}
				if(n.getParent() !=null && n.getParent().getParent()!=null) {
					intermediateCoordinates.add(n.getParent().getCoordinate());
				}
				n = n.getParent();
			}
			hm.put(new PairOfCoords_naive(sourceCoord,goalCoord,blockSize),new Pair<Double,ArrayList<Coordinate>>(distanceAccumulator,intermediateCoordinates));
		}

		if(mapCounter==8) {
			String c1 = "("+sourceCoord.getX() + "," + sourceCoord.getY() +") ";
			String c2 = "("+goalCoord.getX() + "," + goalCoord.getY() +") ";
			System.out.println("Making: "+c1 + c2 + distanceAccumulator);
			
			
		}
	}
	
	private static void saveDB(ArrayList<HashMap<PairOfCoords_naive,Pair<Double,ArrayList<Coordinate>>>> db) {
		String filename = "/Users/olly_freeman/Dropbox/Part2Project/"+blockSize+"zero_naive.ser";
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(db);
			outStream.close();
			fileOut.close();
		} catch(IOException i) {
			i.printStackTrace();
		}
	}
	
	
	/*private static void loadDB() {
		try {
			String filename = "/Users/olly_freeman/Dropbox/Part2Project/"+blockSize+"x"+blockSize+"zerofulldb.ser";
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			ArrayList<HashMap<OldPairOfCoords,LengthAndIntermediateNodes>> db = (ArrayList<HashMap<OldPairOfCoords,LengthAndIntermediateNodes>>) in.readObject();
			in.close();
			fileIn.close();
			for(Entry<OldPairOfCoords, LengthAndIntermediateNodes> e : db.get(11).entrySet()) {
				System.out.println("Loading: " + e.getKey().toString() + ", " + e.getValue().getLength() +" with code " + e.getValue().getIntermediateNodes());
			}
		} catch(IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			return;
		}

	}*/

	private static double getDistance(Node n1, Node n2) {
		double xDiff = n1.getX() - n2.getX();
		double yDiff = n1.getY() - n2.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}
	
	public static void main(String[] args) {
		createDB();
	}

}
