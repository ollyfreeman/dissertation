package preparation;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MyClassParameterizedTest {

  private int multiplier1;
  private int multiplier2;

  public MyClassParameterizedTest(int testParameter1, int testParameter2) {
    this.multiplier1 = testParameter1;
    this.multiplier2 = testParameter2;
  }

  // creates the test data
  @Parameters
  public static Collection<Object[]> data() {
    Object[][] data = new Object[][] { { 1,2 }, { 5,6 }, { 121,32 } };
    return Arrays.asList(data);
  }

  @Test
  public void testMultiplyException() {
    MyClass tester = new MyClass();
    assertEquals("Result", multiplier1 * multiplier2,
        tester.multiply(multiplier1, multiplier2));
  }

}
