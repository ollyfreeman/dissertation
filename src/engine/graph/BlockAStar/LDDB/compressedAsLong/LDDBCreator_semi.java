package engine.graph.BlockAStar.LDDB.compressedAsLong;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import engine.map.Map;
import engine.graph.Graph;
import engine.graph.Node;
import engine.graph.GraphGenerator;
import engine.graph.AStar.AStar;
import engine.graph.BlockAStar.LDDB.PairOfCoords;
import utility.Coordinate;

public class LDDBCreator_semi {

	private static int blockSize = 4;

	public static void createDB() {
		int totalMaps = (int) Math.pow(2,blockSize*blockSize);
		ArrayList<HashMap<PairOfCoords,Long>> db = new ArrayList<HashMap<PairOfCoords,Long>>();
		for(int mapCounter=0;mapCounter<totalMaps;mapCounter++) {
			System.out.println(mapCounter + " of " + totalMaps);
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
			HashMap<PairOfCoords,Long> hm = new HashMap<PairOfCoords,Long>();
			for(int i=0;i<=blockSize;i++) {
				for(int j=0;j<=blockSize;j++) {
					for(int k=0;k<blockSize;k++) {
						Coordinate[] goalArray = {new Coordinate(k,0),new Coordinate(blockSize,k),new Coordinate(blockSize-k,blockSize),new Coordinate(0,blockSize-k)};
						for(Coordinate goalCoord : goalArray) {
							f(hm,m,new Coordinate(i,j),goalCoord,blockSize,mapCounter);
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
	private static void f(HashMap<PairOfCoords,Long> hm, Map map, Coordinate sourceCoord, Coordinate goalCoord, int size, int mapCounter) {
		Node sourceNode = new Node(sourceCoord);
		Node goalNode = new Node(goalCoord);
		Graph g = GraphGenerator.generateGraph_visibility_edge_zeroWidth(map, sourceNode, goalNode); //this was GraphGenerator.generateBlockAStarGraph_visibility_edge_zeroWidth

		g.setSource(g.getNode(sourceCoord));
		g.setGoal(g.getNode(goalCoord));
		//Node n = AStarAlgorithm.getPath(g,map);
		AStar aStar = new AStar();	aStar.go(g,map);
		float distanceAccumulator = 0.0F;
		ArrayList<Coordinate> intermediateCoordinates = new ArrayList<Coordinate>();
		if(aStar.goalNodeExists()) {
			distanceAccumulator = (float) aStar.getDistance();
			LinkedList<Coordinate> list = aStar.getPath();
			for(int i=1; i<list.size()-1; i++) {
				intermediateCoordinates.add(list.get(i));
			}
		
		/*if(n!=null) {
			while(n != null) {
				try {
					distanceAccumulator+=getDistance(n,n.getParent());
				} catch (NullPointerException e) {
					//when we get to the final (source) node
				}
				if(n.getParent() !=null && n.getParent().getParent() !=null) {
					if(LineOfSight.isVisible_edge_zeroWidth(n, n.getParent().getParent(), map, false)) {
						n.setParent(n.getParent().getParent());
					}
					intermediateCoordinates.add(n.getParent().getCoordinate());
				}
				n = n.getParent();
			}*/
			long l = 0;
			l = l | Float.floatToIntBits(distanceAccumulator);;
			l = l << 32;
			l = l | LDDB.getListCode(intermediateCoordinates);
			hm.put(new PairOfCoords(sourceCoord,goalCoord,blockSize), l);
		}
	}

	private static void saveDB(LDDB lddb) {
		System.out.println("Saving");
		String filename = "/Users/olly_freeman/Dropbox/Part2Project/"+blockSize+"zero_semiSC.ser";
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

	public static void main(String[] args) {
		createDB();
	}

}
