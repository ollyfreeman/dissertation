package preparation;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

public class TestDrawing {
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 280;
        int height = 150;
        Map map = MapGenerator.generateMap(width, height, 30, 200);

        Graph graph = GraphGenerator.generateGraph_v2(map);
		Node n=ThetaStarAlgorithm.getPath(graph);
		List<Cell> list = new LinkedList<Cell>();
		if(n==null) {
			System.out.println("No path, exiting...");
		} else {
			
			while(n != null) {
				Cell cell = map.getCell(n.getX(), n.getY());
				list.add(0, cell);
				n = n.getParent();
			}
		}
        DrawingPanel p = new DrawingPanel(map, width, height,list);
        f.add(p);
        f.pack();
        f.setVisible(true);
	}
	
}
