package preparation;

public class Test {
	
	public static void main(String[] args) {
		Map map = MapGenerator.generateMap(40, 40, 40, 100);
		
		Graph graph = GraphGenerator.generateGraph_v2(map);
		Node n,goal,n2,n3;
		n=goal=n2=n3= AStarAlgorithm_v2.getPath(graph);
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
			
			AStarSmoothing.smoothe(graph.getHead(), goal);
			for(int y=0;y < 40; y++) {
				for(int x=0; x < 40; x++) {
					Cell cell = map.getCell(x, y);
					cell.setPath(false);
				}
			}
			while(n3 != null) {
				Cell cell = map.getCell(n3.getX(), n3.getY());
				cell.setPath(true);
				n3 = n3.getParent();
			}
			map.printMap();
		}
	}
	
}
