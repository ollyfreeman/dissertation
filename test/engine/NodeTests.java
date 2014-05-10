package engine;

import static org.junit.Assert.*;

import org.junit.Test;

import engine.graph.Node;
import utility.Coordinate;

public class NodeTests {
	
	/* this JUnit test class tests the basic functionality of the Node class
	 */

	@Test
	public void testFindLocation() {
		Node n1 = new Node(new Coordinate(7,4));
		assertEquals("n1.getX() incorrect", n1.getX(), 7);
		assertEquals("n1.getNeighbours() incorrect", n1.getNeighbours().size(), 0);
		n1.addNeighbour(new Node(new Coordinate(1,2)));
		assertEquals("n1.addNeighbour() incorrect", n1.getNeighbours().size(), 1);
		assertEquals("n1.addNeighbour() incorrect", n1.getNeighbours().get(0).getX(), 1);
		n1.setF(98.7);
		assertEquals("n1.setF() or n1.getF() incorrect", n1.getF() == 98.7,true);
	}

}
