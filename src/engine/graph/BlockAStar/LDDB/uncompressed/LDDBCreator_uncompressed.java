package engine.graph.BlockAStar.LDDB.uncompressed;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import data.CompressionType;
import data.ExtensionType;
import engine.map.Map;
import engine.graph.Graph;
import engine.graph.Node;
import engine.graph.GraphGenerator;
import engine.graph.AStar.AStar;
import engine.graph.BlockAStar.LDDB.uncompressed.PairOfCoords_uncompressed;
import utility.Coordinate;
import utility.Pair;

/*
 * Most naive implementation of the LDDB, where I use an ArrayList of HashMaps of unencoded types:
 * PairOfCoords_naive : i.e. where the PairOfCoords HAS 2 Coordinates
 * Float
 * ArrayList<Coordinate>
 */

public class LDDBCreator_uncompressed {

	private final static CompressionType compressionType = CompressionType.uncompressed;
	private static int blockSize = 4;
	
	public static void createDB(ExtensionType extensionType) {
		List<Coordinate> sourceList = new LinkedList<Coordinate>();
		List<Coordinate> goalList = new LinkedList<Coordinate>();
		switch(extensionType) {
		case standard:	for(int i=0; i<blockSize;i++) {
							sourceList.add(new Coordinate(i,0)); sourceList.add(new Coordinate(blockSize,i)); sourceList.add(new Coordinate(blockSize-i,blockSize)); sourceList.add(new Coordinate(0,blockSize-i));
							goalList.add(new Coordinate(i,0)); goalList.add(new Coordinate(blockSize,i)); goalList.add(new Coordinate(blockSize-i,blockSize)); goalList.add(new Coordinate(0,blockSize-i));
						}
						break;
		case semi:		for(int i=0; i<blockSize;i++) {
							goalList.add(new Coordinate(i,0)); goalList.add(new Coordinate(blockSize,i)); goalList.add(new Coordinate(blockSize-i,blockSize)); goalList.add(new Coordinate(0,blockSize-i));
						}
						for(int i=0;i<=blockSize;i++) {
							for (int j=0;j<=blockSize;j++) {
								sourceList.add(new Coordinate(i,j));
							}
						}
						break;
		case full:		for(int i=0;i<=blockSize;i++) {
							for (int j=0;j<=blockSize;j++) {
								sourceList.add(new Coordinate(i,j));
								goalList.add(new Coordinate(i,j));
							}
						}
		break;
		default:		break;
		}
		int totalMaps = (int) Math.pow(2,blockSize*blockSize);
		ArrayList<HashMap<PairOfCoords_uncompressed,Pair<Float,ArrayList<Coordinate>>>> db = new ArrayList<HashMap<PairOfCoords_uncompressed,Pair<Float,ArrayList<Coordinate>>>>();
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
			HashMap<PairOfCoords_uncompressed,Pair<Float,ArrayList<Coordinate>>> hm = new HashMap<PairOfCoords_uncompressed,Pair<Float,ArrayList<Coordinate>>>();
			for(Coordinate sourceCoord : sourceList) {
				for(Coordinate goalCoord : goalList) {
					f(hm,m,sourceCoord,goalCoord,blockSize,mapCounter);
				}
			}
			db.add(hm);
			System.out.println(mapCounter + " of " + totalMaps);
		}
		LDDB_uncompressed lddb = new LDDB_uncompressed(db);
		saveLDDB(lddb,extensionType);
	}

	//mapCounter parameter only for debugging
	private static void f(HashMap<PairOfCoords_uncompressed,Pair<Float,ArrayList<Coordinate>>> hm, Map map, Coordinate sourceCoord, Coordinate goalCoord, int size, int mapCounter) {
		Node sourceNode = new Node(sourceCoord);
		Node goalNode = new Node(goalCoord);
		Graph g = GraphGenerator.generateGraph_visibility_edge_zeroWidth(map, sourceNode, goalNode); //this was GraphGenerator.generateBlockAStarGraph_visibility_edge_zeroWidth

		g.setSource(g.getNode(sourceCoord));
		g.setGoal(g.getNode(goalCoord));
		AStar aStar = new AStar();	aStar.go(g,map);
		float distanceAccumulator = 0.0F;
		ArrayList<Coordinate> intermediateCoordinates = new ArrayList<Coordinate>();
		if(aStar.goalNodeExists()) {
			distanceAccumulator = (float) aStar.getDistance();
			LinkedList<Coordinate> list = aStar.getPath();
			for(int i=1; i<list.size()-1; i++) {
				intermediateCoordinates.add(list.get(i));
			}
			hm.put(new PairOfCoords_uncompressed(sourceCoord,goalCoord),new Pair<Float,ArrayList<Coordinate>>(distanceAccumulator,intermediateCoordinates));
			//if(mapCounter==1) System.out.println(sourceCoord + " to " + goalCoord);
		}
	}
	
	private static void saveLDDB(LDDB_uncompressed lddb, ExtensionType extensionType) {
		String filename = "/Users/olly_freeman/Dropbox/Part2Project/"+extensionType+"_"+blockSize+"_"+compressionType+".ser";
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
		createDB(ExtensionType.full);
	}

}
