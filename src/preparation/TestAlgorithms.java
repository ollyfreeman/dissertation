package preparation;

public class TestAlgorithms {
	
	public static void main(String[] args) {
		Map map = MapGenerator.generateMap(60, 40, 30, 50);

		//A*
		Graph graph = GraphGenerator.generateGraph_v2(map);
		Node n;		
		double startTime, endTime, duration,angle,totalLength;
		
		startTime = System.nanoTime();
		n = AStarAlgorithm_v2.getPath(graph);
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		
		if(n==null) {
			System.out.println("No path, exiting...");
		} else {
			totalLength = 0.0;
			angle = 0.0;
			while(n != null) {
				Cell cell = map.getCell(n.getX(), n.getY());
				cell.setPath(true);
				try {
					totalLength+=getDistance(n,n.getParent());
				} catch (NullPointerException e) {
					//when we get to the final (source) node
				}
				try {
					angle += getAngle(n);
				}catch (NullPointerException e) {
					//when we get to the penultimate node
				}
				n = n.getParent();
			}
			map.printMap();
			System.out.println("---------");
			System.out.println("A* length: " + totalLength + " with " + angle + "degrees, in: " + duration +"ms");
			System.out.println("---------");
			//reset MAP,
			for(int y=0;y < 40; y++) {
				for(int x=0; x < 60; x++) {
					Cell cell = map.getCell(x, y);
					cell.setPath(false);
				}
			}
			
			//A* SMOOTHING
			graph = GraphGenerator.generateGraph_v2(map);
			startTime = System.nanoTime();
			n = AStarAlgorithm_v2.getPath(graph);
			endTime = System.nanoTime();
			duration = (endTime - startTime)/1000000;
			double startTime2 =  System.nanoTime();
			AStarSmoothing.smoothe(graph.getHead(), n);
			double endTime2 = System.nanoTime();
			double duration2 = (endTime2 - startTime2)/1000000;
			
			totalLength = 0;
			angle = 0;
			while(n != null) {
				Cell cell = map.getCell(n.getX(), n.getY());
				cell.setPath(true);
				try {
					totalLength+=getDistance(n,n.getParent());
				} catch (NullPointerException e) {
					//when we get to the final (source) node
				}
				try {
					angle += getAngle(n);
				}catch (NullPointerException e) {
					//when we get to the penultimate node
				}
				n = n.getParent();
			}
			map.printMap();
			
			System.out.println("---------");
			System.out.println("A* smoothing length: " + totalLength + " with " + angle + "degrees, in: " + duration +"ms plus " + duration2 + "ms");
			System.out.println("---------");
			//reset MAP,
			for(int y=0;y < 40; y++) {
				for(int x=0; x < 60; x++) {
					Cell cell = map.getCell(x, y);
					cell.setPath(false);
				}
			}
			
			//THETA*
			graph = GraphGenerator.generateGraph_v2(map);
			startTime = System.nanoTime();
			n = ThetaStarAlgorithm.getPath(graph);
			endTime = System.nanoTime();
			duration = (endTime - startTime)/1000000;
			
			if(n==null) {
				System.out.println("Theta* didn't find a path! Exiting...");
			} else {
				totalLength = 0;
				angle = 0;
				while(n != null) {
					Cell cell = map.getCell(n.getX(), n.getY());
					cell.setPath(true);
					try {
						totalLength+=getDistance(n,n.getParent());
					} catch (NullPointerException e) {
						//when we get to the final (source) node
					}
					try {
						angle += getAngle(n);
					}catch (NullPointerException e) {
						//when we get to the penultimate node
					}
					n = n.getParent();
				}
				map.printMap();
				System.out.println("---------");
				System.out.println("Theta* length: " + totalLength + " with " + angle + "degrees, in: " + duration +"ms");
				System.out.println("---------");
			}
		}
	}
	
	private static double getDistance(Node n1, Node n2) {
		double xDiff = n1.getX() - n2.getX();
		double yDiff = n1.getY() - n2.getY();
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}
	
	private static double getAngle(Node n) {
		Node p = n.getParent();
		Node pp = p.getParent();
		double dotProduct = (p.getX()-n.getX())*(pp.getX()-p.getX()) + (p.getY()-n.getY())*(pp.getY()-p.getY());
		double denominator = getDistance(n, p)*getDistance(p, pp);
		double cosAngle = (dotProduct/denominator);
		if(cosAngle > 1) {
			System.out.println("Nodes: " + n.toString() + ", " + p.toString() + " and " + pp.toString() + " gave an cos of angle of " + cosAngle);
		}
		double angle = (Math.acos(cosAngle))*(180/Math.PI);
		return angle; //abs returns a value between
	}
	
}
