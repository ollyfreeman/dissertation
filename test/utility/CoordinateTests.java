package utility;

import static org.junit.Assert.*;

import org.junit.Test;
import utility.Coordinate;

public class CoordinateTests {
	
	/* this JUnit test class tests the basic functionality of the Coordinate class
	 */

	@Test
	public void testFindLocation() {
		Coordinate c = new Coordinate(7,4);
		assertEquals("c.getX() incorrect", c.getX(), 7);
		assertEquals("c.getY() incorrect", c.getY(), 4);
		c.setY(5);
		assertEquals("c.setY() incorrect", c.getY(), 5);
	}

}
