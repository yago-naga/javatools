package javatools.datatypes;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;


public class MultiMapTest {

  @Test
  public void iteratorTest() {
    MultiMap<String, String> mm = new MultiMap<String, String>();

    // Initialize map
    mm.put("A", "b");
    mm.put("A", "c");
    mm.put("B", "z");
    mm.put("B", "y");
    
    // Check values for key A
    Set<String> valuesA = new HashSet<String>();
    valuesA.add("b");
    valuesA.add("c");
    
    assertEquals(mm.get("A"), valuesA);

    // Check values for key B
    Set<String> valuesB = new HashSet<String>();
    valuesB.add("z");
    valuesB.add("y");
    
    assertEquals(mm.get("B"), valuesB);
  }
}
