package preparation;

public class Test {
	
	public static void main(String[] args) {
		Map map = MapGenerator.generateMap(40, 40, 20, 100);
		
		Graph graph = GraphGenerator.generateGraph(map);
		Node n = AStarAlgorithm_v2.getPath(graph);
		
		while(n != null) {
			Cell cell = map.getCell(n.getX(), n.getY());
			cell.setPath();
			n = n.getParent();
		}
		map.printMap();
	}
	
}
