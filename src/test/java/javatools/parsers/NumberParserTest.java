package javatools.parsers;

import static org.junit.Assert.*;

import org.junit.Test;


public class NumberParserTest {

  @Test
  public void testGetNumberAndUnit() {
    int[] pos = new int[2];
    
    String[] nu = NumberParser.getNumberAndUnit("1923 (film)", pos);
    
    assertEquals("1923", nu[0]);
    assertEquals(0, pos[0]);
    assertEquals(4, pos[1]);
    
    nu = NumberParser.getNumberAndUnit("12#km", pos);
    assertEquals("12", nu[0]);
    assertEquals("km", nu[1]);
    assertEquals(0, pos[0]);
    assertEquals("12#km".length(), pos[1]);
  }
}
