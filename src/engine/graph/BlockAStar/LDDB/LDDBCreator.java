package engine.graph.BlockAStar.LDDB;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import engine.map.Map;
import engine.graph.Graph;
import engine.graph.LineOfSight;
import engine.graph.Node;
import engine.graph.GraphGenerator;
import engine.graph.Dijkstra.DijkstraAlgorithm;
import engine.graph.BlockAStar.LDDB.PairOfCoords;
import utility.Coordinate;
import utility.Pair;

public class LDDBCreator {

	private static int blockSize = 4;
	
	public static void createDB() {
		int totalMaps = (int) Math.pow(2,blockSize*blockSize);
		ArrayList<HashMap<PairOfCoords,Pair<Float,Integer>>> db = new ArrayList<HashMap<PairOfCoords,Pair<Float,Integer>>>();
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
			HashMap<PairOfCoords,Pair<Float,Integer>> hm = new HashMap<PairOfCoords,Pair<Float,Integer>>();
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
		}
		LDDB lddb = new LDDB(db);
		saveDB(lddb);
		//loadDB();
	}

	//mapCounter parameter only for debugging
	private static void f(HashMap<PairOfCoords,Pair<Float,Integer>> hm, Map map, Coordinate sourceCoord, Coordinate goalCoord, int size, int mapCounter) {
		Node sourceNode = new Node(sourceCoord);
		Node goalNode = new Node(goalCoord);
		Graph g = GraphGenerator.generateGraph_visibility_edge_zeroWidth(map, sourceNode, goalNode); //this was GraphGenerator.generateBlockAStarGraph_visibility_edge_zeroWidth

		g.setSource(g.getNode(sourceCoord));
		g.setGoal(g.getNode(goalCoord));
		Node n = DijkstraAlgorithm.getPath(g);
		float distanceAccumulator = 0.0F;
		ArrayList<Coordinate> intermediateCoordinates = new ArrayList<Coordinate>();
		if(n!=null) {
			while(n != null) {
				try {
					distanceAccumulator+=getDistance(n,n.getParent());
				} catch (NullPointerException e) {
					//when we get to the final (source) node
				}
				if(n.getParent() !=null && n.getParent().getParent()!=null) {
					//if(LineOfSight.isVisible_edge_zeroWidth(n, n.getParent().getParent(), map)) {
						//n.setParent(n.getParent().getParent());
					//}
					intermediateCoordinates.add(n.getParent().getCoordinate());
				}
				n = n.getParent();
			}
			Pair<Float,Integer> pair = new Pair<Float,Integer>(distanceAccumulator,LDDB.getListCode(intermediateCoordinates));
			hm.put(new PairOfCoords(sourceCoord,goalCoord,blockSize), pair);
		}
	}
	
	private static void saveDB(LDDB lddb) {
		System.out.println("Saving");
		String filename = "/Users/olly_freeman/Dropbox/Part2Project/"+blockSize+"zero.ser";
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(lddb);
			outStream.close();
			fileOut.close();
		} catch(IOException i) {
			i.printStackTrace();
		}
	}
	
	private static void loadDB() {
		try {
			String filename = "/Users/olly_freeman/Dropbox/Part2Project/"+blockSize+"zero.ser";
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			LDDB lddb = (LDDB) in.readObject();
			in.close();
			fileIn.close();
		} catch(IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			return;
		}

	}
	
	private static double getDistance(Node n1, Node n2) {
		double xDiff = n1.getX() - n2.getX();
		double yDiff = n1.getY() - n2.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}
	
	public static void main(String[] args) {
		createDB();
	}

}