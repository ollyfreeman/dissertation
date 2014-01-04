package preparation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map.Entry;

import engine.map.Map;
import engine.graph.DijkstraAlgorithm;
import engine.graph.Graph;
import engine.graph.Node;
import engine.graph.GraphGenerator;
import engine.graph.PairOfCoords;
import utility.Coordinate;

public class ldbbCreator {

	public static void main(String[] args) {
		int size = 2;
		int totalMaps = (int) Math.pow(2,size*size);
		LinkedList<HashMap<PairOfCoords,Double>> db = new LinkedList<HashMap<PairOfCoords,Double>>();
		for(int mapCounter=0;mapCounter<totalMaps;mapCounter++) {
			//System.out.println(mapCounter+1 + " of " + totalMaps);
			int mapRep = mapCounter;
			int[][] map = new int[size][size];
			for(int j=size-1;j>=0;j--) {
				for(int i=size-1;i>=0;i--) {
					if((mapRep & 1) == 1) {
						map[i][j] = 1;			//1 in all reps means unblocked
					} else {
						map[i][j] = 0; 			//0 in all reps means blocked
					}
					mapRep = mapRep>>>1;
				}
			}
			Map m = new Map(map);
			HashMap<PairOfCoords,Double> hm = new HashMap<PairOfCoords,Double>();
			/*if(mapCounter==10) {
				m.print();
			}*/
			for(int i=0; i<size;i++) {
				for(int j=0;j<size;j++) {
					Coordinate[] sourceArray = {new Coordinate(i,0),new Coordinate(size,i),new Coordinate(size-i,size),new Coordinate(0,size-i)};
					Coordinate[] goalArray = {new Coordinate(j,0),new Coordinate(size,j),new Coordinate(size-j,size),new Coordinate(0,size-j)};
					for(Coordinate sourceCoord : sourceArray) {
						for(Coordinate goalCoord : goalArray) {
							f(hm,m,sourceCoord,goalCoord,size,mapCounter);
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
	private static void f(HashMap<PairOfCoords,Double> hm, Map m, Coordinate sourceCoord, Coordinate goalCoord, int size, int mapCounter) {
		Node sourceNode = new Node(sourceCoord);
		Node goalNode = new Node(goalCoord);
		Graph g = GraphGenerator.generateBlockAStarGraph_visibility_edge_finiteWidth(m, sourceNode, goalNode);

		double distanceAccumulator = 0.0;
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
				n = n.getParent();
			}
			hm.put(new PairOfCoords(sourceCoord,goalCoord),distanceAccumulator);
		}

		/*if(mapCounter==10) {
			String c1 = "("+sourceCoord.getX() + "," + sourceCoord.getY() +") ";
			String c2 = "("+goalCoord.getX() + "," + goalCoord.getY() +") ";
			System.out.println("Making: "+c1 + c2 + distanceAccumulator);
			
		}*/
	}
	
	private static void saveDB(LinkedList<HashMap<PairOfCoords,Double>> db) {
		String filename = "/Users/olly_freeman/Dropbox/Part2Project/3by3db.ser";
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
			String filename = "/Users/olly_freeman/Dropbox/Part2Project/2by2db.ser";
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			LinkedList<HashMap<PairOfCoords,Double>> db = (LinkedList<HashMap<PairOfCoords,Double>>) in.readObject();
			in.close();
			fileIn.close();
			for(Entry<PairOfCoords, Double> e : db.get(10).entrySet()) {
				System.out.println("Loading: " + e.getKey().toString() + ", " + e.getValue());
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
