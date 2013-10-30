package preparation;

public class TestAlgorithms {
	
	public static void main(String[] args) {
		Map map = MapGenerator.generateMap(40, 40, 20, 2);
		
		Graph graph = GraphGenerator.generateGraph_v2(map);
		Node n,n1;
		n = n1 = AStarAlgorithm_v2.getPath(graph);
		if(n==null) {
			System.out.println("No path, exiting...");
		} else {
			while(n != null) {
				Cell cell = map.getCell(n.getX(), n.getY());
				cell.setPath(true);
				n = n.getParent();
			}
			map.printMap();
			
			System.out.println("---------");
			//reset MAP,
			for(int y=0;y < 40; y++) {
				for(int x=0; x < 40; x++) {
					Cell cell = map.getCell(x, y);
					cell.setPath(false);
				}
			}
			
			AStarSmoothing.smoothe(graph.getHead(), n1);
			
			while(n1 != null) {
				Cell cell = map.getCell(n1.getX(), n1.getY());
				cell.setPath(true);
				n1 = n1.getParent();
			}
			map.printMap();
			
			System.out.println("---------");
			//reset MAP,
			for(int y=0;y < 40; y++) {
				for(int x=0; x < 40; x++) {
					Cell cell = map.getCell(x, y);
					cell.setPath(false);
				}
			}
			
			graph = GraphGenerator.generateGraph_v2(map);
			n = ThetaStarAlgorithm.getPath(graph);
			
			if(n==null) {
				System.out.println("Theta* didn't find a path! Exiting...");
			} else {
				while(n != null) {
					Cell cell = map.getCell(n.getX(), n.getY());
					cell.setPath(true);
					n = n.getParent();
				}
				map.printMap();
			}
		}
	}
	
}
