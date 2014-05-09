package engine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import engine.graph.Graph;
import engine.graph.GraphGenerator;
import engine.graph.Node;
import engine.map.Map;
import utility.Coordinate;

public class GraphTests {
	
	/* this JUnit test class test that the basic functionality of the Graph class and the GraphGenerator
	 * works correctly, including the functionality of the Node class
	 */
	
	@Test
	public void test() {
		int[][] array2D = new int[4][3];
		array2D[0][0]=1; array2D[1][0]=2; array2D[2][0]=4; array2D[3][0]=0;
		array2D[0][1]=0; array2D[1][1]=3; array2D[2][1]=0; array2D[3][1]=1;
		array2D[0][2]=3; array2D[1][2]=2; array2D[2][2]=0; array2D[3][2]=1;
		Map map = new Map(array2D);
		Graph graph = GraphGenerator.generateGraph_edge_finiteWidth(map, new Node(new Coordinate(0,0)), new Node(new Coordinate(4,3)));
		assertEquals("Source x-coord is not corrent", graph.getSource().getX(),0);	
		assertEquals("Source y-coord is not corrent", graph.getSource().getY(),0);	
		assertEquals("(1,1) does not have correct neighbours",graph.getSource().getNeighbours().get(0).toString(), "Coordinate: (1,1), Neighbours: (0,0) (2,0) (2,2) (1,0) (2,1) (1,2) ");
		assertEquals("Goal f-value has not been initialised correctly",graph.getGoal().getF()==Double.POSITIVE_INFINITY,true);
		graph.getNode(new Coordinate(1,1)).setParent(graph.getSource());
		assertEquals("Set parent does not work correctly",graph.getSource().getNeighbours().get(0).getParent().toString(), "Coordinate: (0,0), Neighbours: (1,1) (1,0) ");
		graph.getSource().addNeighbour(graph.getGoal());
		assertEquals("Add neighbour does not work correctly",graph.getSource().toString(), "Coordinate: (0,0), Neighbours: (1,1) (1,0) (4,3) ");
	}

}
