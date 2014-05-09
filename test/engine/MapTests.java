package engine;

import static org.junit.Assert.*;

import org.junit.Test;

import engine.map.Map;
import utility.Coordinate;

public class MapTests {
	
	/* this JUnit test class test that the basic functionality of the Map class works correctly,
	 * including the functionality of the Cell class
	 */

	@Test
	public void testFindLocation() {
		int[][] array2D = new int[4][3];
		array2D[0][0]=1; array2D[1][0]=2; array2D[2][0]=4; array2D[3][0]=0;
		array2D[0][1]=0; array2D[1][1]=3; array2D[2][1]=1; array2D[3][1]=1;
		array2D[0][2]=3; array2D[1][2]=2; array2D[2][2]=2; array2D[2][2]=1;
		Map map = new Map(array2D);
		assertEquals("Cell with Coordinate(1,2) has been incorrectly instantiated", map.getCell(1, 2).isBlocked(), false);
		assertEquals("Cell with Coordinate(0,1) has been incorrectly instantiated", map.getCell(new Coordinate(0,1)).isBlocked(), true);	
		assertEquals("Cell width has been incorrectly instantiated",map.getWidth(), 4);
		assertEquals("Cell height has been incorrectly instantiated",map.getHeight(), 3);
	}

}
