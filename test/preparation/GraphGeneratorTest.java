package preparation;

import static org.junit.Assert.*;

import org.junit.Test;

public class GraphGeneratorTest {

	@Test
	public void test() {
		int[][] array2D = new int[4][3];
		array2D[0][0]=1; array2D[1][0]=2; array2D[2][0]=4; array2D[3][0]=0;
		array2D[0][1]=0; array2D[1][1]=3; array2D[2][1]=0; array2D[3][1]=1;
		array2D[0][2]=3; array2D[1][2]=2; array2D[2][2]=0; array2D[2][2]=1;
		Map map = new Map(array2D);
		Graph graph = GraphGenerator.generateGraph(map);
		Node head = graph.getHead();
		assertEquals("Head does not have correct neighbours", head.toString(), "Coordinate: (0,0), Successors: (1,0) (1,1) ");
	}

}
