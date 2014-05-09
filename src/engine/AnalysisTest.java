package engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import utility.*;
import data.AlgorithmType;
import data.CompressionType;
import engine.graph.BlockAStar.Block;
import engine.graph.BlockAStar.BlockAStar_standard;
import engine.map.MapGenerator;

public class AnalysisTest {

	private static MapInstance mapInstance;
	private static final String filePath = "/Users/olly_freeman/Dropbox/Part2Project/";

	public static void main(String[] args) {
		/*for(int coverage=0; coverage<=70; coverage+=10) {
			for(int clustering = 1000; clustering <=1000; clustering+=1) {
				generateMaps(100,coverage,clustering,400);

			}
		}*/
		//generateMaps(200,20,50,100);
		//validPaths(100, 400);
		//for(int i=20; i<=30; i+=10) {
		//for(int j=0; j<=100; j+=50) {
		compareAlgorithms(200,20,50,100,false);
		//}
		//}
		//blockAStar(200, 20, 50, 100, false);
	}

	public static void generateMaps(int size, int coverage, int clustering, int numberOfMaps) {
		for(int i=0;i<numberOfMaps;i++) {
			saveMapOnly(size+"/"+coverage+"/"+clustering+"/Random/"+i+".ser",new MapInstance(MapGenerator.generateMap(size, size, coverage, clustering)));
		}
	}

	public static void compareAlgorithms(int size, int coverage, int clustering, int numberOfMaps, boolean randomStartAndGoal) {
		LinkedList<Pair<Coordinate,Coordinate>> startAndGoals = new LinkedList<Pair<Coordinate,Coordinate>>();
		if(randomStartAndGoal){
			for(int i=0;i<numberOfMaps;i++) {
				Coordinate start = new Coordinate((int) (size*Math.random()), (int) (size*Math.random()));
				Coordinate goal = new Coordinate((int) (size*Math.random()), (int) (size*Math.random()));
				startAndGoals.add(new Pair<Coordinate,Coordinate>(start,goal));
			}
		}
		int noPathCounter = 0;
		List<String[]> data = new ArrayList<String[]>();
		String[] array = new String[7];
		array[0] = "Map"; array[1] = "Algorithm"; array[2] = "GraphCreationTime"; array[3] = "AlgorithmTime"; array[4] = "Distance"; array[5] = "Angle"; array[6] = "NodesExpanded";
		data.add(array);
		AlgorithmType[] atArray = {AlgorithmType.Dijkstra, AlgorithmType.AStar, AlgorithmType.AStarSmoothed, AlgorithmType.ThetaStar, AlgorithmType.LazyThetaStar, AlgorithmType.BlockAStar};
		//AlgorithmType[] atArray = {AlgorithmType.AStar};
		for(int i=0; i<numberOfMaps;i++) {	
			double lengths[] = new double[atArray.length]; int algorithmCounter=0;
			for(AlgorithmType at : atArray) {
				array = new String[7];
				array[0] = "Map " + i;
				array[1] = at.toString();
				loadMapInstance(size+"/"+coverage+"/"+clustering+"/Random/"+i+".ser");
				if(randomStartAndGoal) {
					mapInstance.createAlgorithmData(at, startAndGoals.get(i).get0(), startAndGoals.get(i).get1(),true);
				} else {
					mapInstance.createAlgorithmData(at, new Coordinate(0,0), new Coordinate(mapInstance.getMap().getWidth(),mapInstance.getMap().getHeight()),true);
				}
				if(mapInstance.getAlgorithmData(at).goalNodeExists()) {
					array[2] = ""+mapInstance.getAlgorithmData(at).getGraphCreationTime();
					array[3] = ""+mapInstance.getAlgorithmData(at).getAlgorithmTime();
					array[4] = ""+mapInstance.getAlgorithmData(at).getDistance();
					array[5] = ""+mapInstance.getAlgorithmData(at).getAngle();
					array[6] = ""+mapInstance.getAlgorithmData(at).getNodesExpanded();
					lengths[algorithmCounter] = mapInstance.getAlgorithmData(at).getDistance();
				} else {
					noPathCounter++;
					lengths[algorithmCounter] = 0.0;
					continue;
					/*array[2] = ""+mapInstance.getAlgorithmData(at).getGraphCreationTime();
					array[3] = ""+mapInstance.getAlgorithmData(at).getAlgorithmTime();
					array[4] = "nopath";
					array[5] = "nopath";
					array[6] = ""+mapInstance.getAlgorithmData(at).getNodesExpanded();*/
				}
				data.add(array);
				assert(algorithmsCorrect(lengths));
				algorithmCounter++;
			}
		}
		System.out.println("No paths for: " + noPathCounter + " maps");
		if(randomStartAndGoal) {
			//saveAsCsvFile(size+"_"+coverage+"_"+clustering+"randomS&G",data);
		} else {
			//saveAsCsvFile(size+"_"+coverage+"_"+clustering,data);
		}
	}
	
	private static boolean algorithmsCorrect(double[] lengths) {
		int lengthIsZeroCounter = 0;
		for(double l : lengths) {
			if(l == 0) {lengthIsZeroCounter++;}
		}
		boolean condition1 = (lengthIsZeroCounter==0 || lengthIsZeroCounter==lengths.length) ? true : false;
		boolean condition2 = ((lengths[0]==lengths[1]) && (lengths[1] >= lengths[2]) && (lengths[1] >= lengths[3]) && (lengths[1] >= lengths[4]) && (lengths[1] >= lengths[5]));
		return condition1 && condition2;
	}

	public static void compareLDDBs(int size, int coverage, int clustering, int numberOfMaps) {
		for(int blockSize=2;blockSize<=4;blockSize++) {
			for(CompressionType compressionType : CompressionType.values()) {
				Block.setSize(blockSize);
				BlockAStar_standard.setSizeAndCompression(blockSize, compressionType);
				//int noPathCounter = 0;
				List<String[]> data = new ArrayList<String[]>();
				String[] array = new String[7];
				array[0] = "Map"; array[1] = "Algorithm"; array[2] = "GraphCreationTime"; array[3] = "AlgorithmTime"; array[4] = "Distance"; array[5] = "Angle"; array[6] = "NodesExpanded";
				data.add(array);
				for(int i=0; i<=1;i++) {
					array = new String[7];
					array[0] = "Map " + i;
					array[1] = (AlgorithmType.BlockAStar).toString();
					loadMapInstance(size+"/"+coverage+"/"+clustering+"/Random/"+i+".ser");
					mapInstance.createAlgorithmData((AlgorithmType.BlockAStar), new Coordinate(0,0), new Coordinate(mapInstance.getMap().getWidth(),mapInstance.getMap().getHeight()),true);
					if(mapInstance.getAlgorithmData((AlgorithmType.BlockAStar)).goalNodeExists()) {
						array[2] = ""+mapInstance.getAlgorithmData((AlgorithmType.BlockAStar)).getGraphCreationTime();
						array[3] = ""+mapInstance.getAlgorithmData((AlgorithmType.BlockAStar)).getAlgorithmTime();
						array[4] = ""+mapInstance.getAlgorithmData((AlgorithmType.BlockAStar)).getDistance();
						array[5] = ""+mapInstance.getAlgorithmData((AlgorithmType.BlockAStar)).getAngle();
						array[6] = ""+mapInstance.getAlgorithmData((AlgorithmType.BlockAStar)).getNodesExpanded();
					} else {
						//noPathCounter++;
						continue;
						/*array[2] = ""+mapInstance.getAlgorithmData(at).getGraphCreationTime();
							array[3] = ""+mapInstance.getAlgorithmData(at).getAlgorithmTime();
							array[4] = "nopath";
							array[5] = "nopath";
							array[6] = ""+mapInstance.getAlgorithmData(at).getNodesExpanded();*/
					}
					data.add(array);
					//System.out.println((AlgorithmType.BlockAStar).toString() + ": " + i +" of "+ numberOfMaps);
				}
				//System.out.println("No paths for: " + noPathCounter + " maps");
				saveAsCsvFile(size+"_"+coverage+"_"+clustering+"_blockAStar"+blockSize+compressionType,data);
			}
		}	
	}

	public static void validPaths(int size, int numberOfMaps) {
		List<String[]> data = new ArrayList<String[]>();
		String[] array = new String[4];
		array[0] = "Coverage"; array[1] = "Clustering"; array[2] = "Count"; array[3] = "CountFreeSandG";
		data.add(array);
		for(int coverage=0; coverage<=70; coverage+=5) {
			for(int clustering=1000; clustering<=1000; clustering+=1) {
				array = new String[4];
				array[0] = "" + coverage;
				array[1] = "" + clustering;
				int pathCounter = 0;
				int startAndGoalCounter = 0;
				for(int i=0; i<numberOfMaps;i++) {
					loadMapInstance(size+"/"+coverage+"/"+clustering+"/Random/"+i+".ser");
					mapInstance.createAlgorithmData(AlgorithmType.AStar, new Coordinate(0,0), new Coordinate(100,100),true);
					if(mapInstance.getAlgorithmData(AlgorithmType.AStar).goalNodeExists()) {
						pathCounter++;
					}
					if(!(mapInstance.getMap().getCell(0, 0).isBlocked()) && !(mapInstance.getMap().getCell(99, 99).isBlocked())) {
						startAndGoalCounter++;
					}
				}
				array[2] = "" + pathCounter;
				array[3] = (pathCounter==0) ? ("" + pathCounter):("" + (((double) pathCounter)/ ((double) startAndGoalCounter)));
				System.out.println("Clustering: " + clustering + ", coverage: " + coverage + ", pathCounter of " + pathCounter);
				data.add(array);
			}
		}
		saveAsCsvFile("ValidPaths_EmptySandG_"+size+"_"+numberOfMaps+"_Cluster1000",data);
	}

	public static void blockAStar(int size, int coverage, int clustering, int numberOfMaps, boolean randomStartAndGoal) {
		LinkedList<Pair<Coordinate,Coordinate>> startAndGoals = new LinkedList<Pair<Coordinate,Coordinate>>();
		if(randomStartAndGoal){
			for(int i=0;i<numberOfMaps;i++) {
				Coordinate start = new Coordinate((int) (size*Math.random()), (int) (size*Math.random()));
				Coordinate goal = new Coordinate((int) (size*Math.random()), (int) (size*Math.random()));
				startAndGoals.add(new Pair<Coordinate,Coordinate>(start,goal));
			}
		}
		int noPathCounter = 0;
		List<String[]> data = new ArrayList<String[]>();
		String[] array = new String[7];
		array[0] = "Map"; array[1] = "Algorithm"; array[2] = "GraphCreationTime"; array[3] = "AlgorithmTime"; array[4] = "Distance"; array[5] = "Angle"; array[6] = "NodesExpanded";
		data.add(array);
		AlgorithmType[] atArray = {AlgorithmType.BlockAStar};
		for(AlgorithmType at : atArray) {
			for(int i=0; i<numberOfMaps;i++) {
				array = new String[7];
				array[0] = "Map " + i;
				array[1] = at.toString();
				loadMapInstance(size+"/"+coverage+"/"+clustering+"/Random/"+i+".ser");
				if(randomStartAndGoal) {
					mapInstance.createAlgorithmData(at, startAndGoals.get(i).get0(), startAndGoals.get(i).get1(),true);
				} else {
					mapInstance.createAlgorithmData(at, new Coordinate(0,0), new Coordinate(mapInstance.getMap().getWidth(),mapInstance.getMap().getHeight()),true);
				}
				if(mapInstance.getAlgorithmData(at).goalNodeExists()) {
					array[2] = ""+mapInstance.getAlgorithmData(at).getGraphCreationTime();
					array[3] = ""+mapInstance.getAlgorithmData(at).getAlgorithmTime();
					array[4] = ""+mapInstance.getAlgorithmData(at).getDistance();
					array[5] = ""+mapInstance.getAlgorithmData(at).getAngle();
					array[6] = ""+mapInstance.getAlgorithmData(at).getNodesExpanded();
				} else {
					noPathCounter++;
					continue;
					/*array[2] = ""+mapInstance.getAlgorithmData(at).getGraphCreationTime();
					array[3] = ""+mapInstance.getAlgorithmData(at).getAlgorithmTime();
					array[4] = "nopath";
					array[5] = "nopath";
					array[6] = ""+mapInstance.getAlgorithmData(at).getNodesExpanded();*/
				}
				data.add(array);
				System.out.println(at.toString() + ": " + i +" of "+ numberOfMaps);
			}
		}
		System.out.println("No paths for: " + noPathCounter + " maps");
		if(randomStartAndGoal) {
			saveAsCsvFile("CSVs/"+size+"_"+coverage+"_"+clustering+"randomS&G_blockAStar4B",data);
		} else {
			saveAsCsvFile("CSVs/"+size+"_"+coverage+"_"+clustering+"_blockAStar4N",data);
		}
	}
	


	private static void loadMapInstance(String filename) {
		try {
			FileInputStream fileIn = new FileInputStream(filePath+"maps/"+filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			mapInstance = null;
			mapInstance = (MapInstance) in.readObject();
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


	public static void saveMapOnly(String filename, MapInstance mapInstance) {
		try {
			File targetFile = new File(filePath+"maps/"+filename);
			File parent = targetFile.getParentFile();
			if(!parent.exists() && !parent.mkdirs()){
				throw new IllegalStateException("Couldn't create dir: " + parent);
			}
			FileOutputStream fileOut = new FileOutputStream(targetFile);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(mapInstance);
			outStream.close();
			fileOut.close();
		} catch(IOException i) {
			i.printStackTrace();
		}
	}

	private static void saveAsCsvFile(String filename, List<String[]> data)
	{
		try {
			File targetFile = new File(filePath+"CSV/"+filename+".csv");
			File parent = targetFile.getParentFile();
			if(!parent.exists() && !parent.mkdirs()){
				throw new IllegalStateException("Couldn't create dir: " + parent);
			}
			CSVWriter writer;
			writer = new CSVWriter(new FileWriter(targetFile));
			writer.writeAll(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
