package preparation;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class MapGeneratorTest {
	
	@BeforeClass
	  public static void testSetup() {
	  }

	@Test
	public void testFindLocation() {
		/*
		This doesn't actually work because the methods are private. I need to look at design patterns to work out how the class works, but this would be the sort of thing
		int[][] map = new int[3][3];
		map[0][0]=1; map[0][1]=2; map[0][2]=4;
		map[1][0]=0; map[1][1]=3; map[1][2]=1;
		map[2][0]=3; map[2][1]=2; map[2][2]=2;	//totalPotential=18
		Location location = MapGenerator.findLocation(1,3,3);
		assertEquals("Wrong x!",location.getX(), 0);
		assertEquals("Wrong y!",location.getX(), 0);
		*/
	}

}
