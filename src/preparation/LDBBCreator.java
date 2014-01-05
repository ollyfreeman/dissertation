package preparation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import engine.map.Map;
import engine.graph.DijkstraAlgorithm;
import engine.graph.Graph;
import engine.graph.Node;
import engine.graph.GraphGenerator;
import engine.graph.LDBB.LengthAndIntermediateNodes;
import engine.graph.LDBB.PairOfCoords;
import utility.Coordinate;

public class LDBBCreator {

	private static int blockSize= 4;
	
	public static void main(String[] args) {
		
		int totalMaps = (int) Math.pow(2,blockSize*blockSize);
		ArrayList<HashMap<PairOfCoords,LengthAndIntermediateNodes>> db = new ArrayList<HashMap<PairOfCoords,LengthAndIntermediateNodes>>();
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
			HashMap<PairOfCoords,LengthAndIntermediateNodes> hm = new HashMap<PairOfCoords,LengthAndIntermediateNodes>();
			if(mapCounter==11) {
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
		}
		saveDB(db);
		loadDB();
	}

	//mapCounter parameter only for debugging
	private static void f(HashMap<PairOfCoords,LengthAndIntermediateNodes> hm, Map m, Coordinate sourceCoord, Coordinate goalCoord, int size, int mapCounter) {
		Node sourceNode = new Node(sourceCoord);
		Node goalNode = new Node(goalCoord);
		Graph g = GraphGenerator.generateBlockAStarGraph_visibility_edge_zeroWidth(m, sourceNode, goalNode);

		double distanceAccumulator = 0.0;
		g.setSource(g.getNode(sourceCoord));
		g.setGoal(g.getNode(goalCoord));
		Node n = DijkstraAlgorithm.getPath(g);
		LinkedList<Coordinate> intermediateCoordinates = new LinkedList<Coordinate>();
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
			hm.put(new PairOfCoords(sourceCoord,goalCoord),new LengthAndIntermediateNodes(distanceAccumulator,intermediateCoordinates));
		}

		if(mapCounter==11) {
			String c1 = "("+sourceCoord.getX() + "," + sourceCoord.getY() +") ";
			String c2 = "("+goalCoord.getX() + "," + goalCoord.getY() +") ";
			System.out.println("Making: "+c1 + c2 + distanceAccumulator+", with intermediate coords: " +intermediateCoordinates);
			
			
		}
	}
	
	private static void saveDB(ArrayList<HashMap<PairOfCoords,LengthAndIntermediateNodes>> db) {
		String filename = "/Users/olly_freeman/Dropbox/Part2Project/"+blockSize+"x"+blockSize+"zerofulldb.ser";
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
	
	private static void loadDB() {
		try {
			String filename = "/Users/olly_freeman/Dropbox/Part2Project/"+blockSize+"x"+blockSize+"zerofulldb.ser";
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			ArrayList<HashMap<PairOfCoords,LengthAndIntermediateNodes>> db = (ArrayList<HashMap<PairOfCoords,LengthAndIntermediateNodes>>) in.readObject();
			in.close();
			fileIn.close();
			for(Entry<PairOfCoords, LengthAndIntermediateNodes> e : db.get(11).entrySet()) {
				System.out.println("Loading: " + e.getKey().toString() + ", " + e.getValue().getLength() +" with code " + e.getValue().getIntermediateNodes());
			}
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

}
