package engine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import utility.Coordinate;
import data.AlgorithmType;
import engine.map.MapGenerator;

public class AnalysisTest {

	private static MapInstance mapInstance;
	private static final String filePath = "/Users/olly_freeman/Dropbox/Part2Project/maps/";

	public static void main(String[] args) {
		//generateMaps(100,30,50,100);
		exportResults(200,10,0,100);
	}
	
	public static void generateMaps(int size, int coverage, int clustering, int numberOfMaps) {
		for(int i=0;i<numberOfMaps;i++) {
			saveMapOnly(size+"/"+coverage+"/"+clustering+"/Random/"+i+".ser",new MapInstance(MapGenerator.generateMap(size, size, coverage, clustering)));
		}
	}

	public static void exportResults(int size, int coverage, int clustering, int numberOfMaps) {
		int noPathCounter = 0;
		List<String[]> data = new ArrayList<String[]>();
		String[] array = new String[7];
		array[0] = "Map"; array[1] = "Algorithm"; array[2] = "GraphCreationTime"; array[3] = "AlgorithmTime"; array[4] = "Distance"; array[5] = "Angle"; array[6] = "NodesExpanded";
		data.add(array);
		AlgorithmType[] atArray = {AlgorithmType.BlockAStar};//AlgorithmType[] atArray = {AlgorithmType.Dijkstra, AlgorithmType.AStar, AlgorithmType.ThetaStar, AlgorithmType.BlockAStar};
		for(AlgorithmType at : atArray) {
			for(int i=0; i<numberOfMaps;i++) {
				array = new String[7];
				array[0] = "Map " + i;
				array[1] = at.toString();
				loadMapInstance(size+"/"+coverage+"/"+clustering+"/Random/"+i+".ser");
				mapInstance.createAlgorithmData(at, new Coordinate(0,0), new Coordinate(mapInstance.getMap().getWidth(),mapInstance.getMap().getHeight()),true);
				if(mapInstance.getAlgorithmData(at).goalNodeExists()) {
					array[2] = ""+mapInstance.getAlgorithmData(at).getGraphCreationTime();
					array[3] = ""+mapInstance.getAlgorithmData(at).getAlgorithmTime();
					array[4] = ""+mapInstance.getAlgorithmData(at).getDistance();
					array[5] = ""+mapInstance.getAlgorithmData(at).getAngle();
					array[6] = ""+mapInstance.getAlgorithmData(at).getNodesExpanded();
					System.out.println("Dist: "+ mapInstance.getAlgorithmData(at).getDistance());
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
			}
		}
		System.out.println("No paths for: " + noPathCounter + " maps");
		saveAsCsvFile("CSVs/"+size+"_"+coverage+"_"+clustering,data);
	}

	private static void loadMapInstance(String filename) {
		try {
			FileInputStream fileIn = new FileInputStream(filePath+filename);
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
			FileOutputStream fileOut = new FileOutputStream(filePath+filename);
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
			String csv = filePath+filename+".csv";
			CSVWriter writer;
			writer = new CSVWriter(new FileWriter(csv));
			writer.writeAll(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
