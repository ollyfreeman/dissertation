package engine;

import static org.junit.Assert.*;

import org.junit.Test;

import engine.map.Cell;
import utility.Coordinate;

public class CellTests {
	
	/* this JUnit test class tests the basic functionality of the Cell class
	 */

	@Test
	public void testFindLocation() {
		Cell cell1 = new Cell(new Coordinate(7,4), true);
		Cell cell2 = new Cell(new Coordinate(2,1), false);
		assertEquals("cell1.isBlocked() incorrect", cell1.isBlocked(), true);
		assertEquals("cell2.isBlocked() incorrect", cell2.isBlocked(), false);
		assertEquals("cell1.getX() incorrect", cell1.getX(), 7);
		assertEquals("cell2.getY() incorrect", cell2.getY(), 1);
	}

}
