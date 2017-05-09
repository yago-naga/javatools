package javatools.util;

import static org.junit.Assert.*;

import org.junit.Test;

import javatools.datatypes.ArrayUtils;


public class ArrayUtilsTest {

  @Test
  public void testIntersectArrays() {
    
    int[] a = new int[] { 0, 1, 2, 3 };
    int[] b = new int[] { 3, 4, 5 };
    int[] c = new int[] { 9 };
    int[] d = new int[] { 1, 2, 5 };
    
    assertEquals(1, ArrayUtils.intersectArrays(a, b));
    assertEquals(0, ArrayUtils.intersectArrays(a, c));
    assertEquals(2, ArrayUtils.intersectArrays(a, d));
    
  }
}
