package javatools.datatypes;

import static org.junit.Assert.*;

import java.util.Map.Entry;

import org.junit.Test;



public class MultiMapTest {
  
  @Test
  public void iteratorTest() {
    MultiMap<String, String> mm = new MultiMap<String, String>();
    
    mm.put("A", "b");
    mm.put("A", "c");
    mm.put("B", "z");
    mm.put("B", "y");

    
    int i=-1;
    
    for (Entry<String, String> e : mm) {
      i++;
      
      switch (i) {
        case 0:
          assertTrue(e.getKey().equals("A"));
          assertTrue(e.getValue().equals("b"));
          break;
        case 1:
          assertTrue(e.getKey().equals("A"));
          assertTrue(e.getValue().equals("c"));
          break;
        case 2:
          assertTrue(e.getKey().equals("B"));
          assertTrue(e.getValue().equals("z"));
          break;
        case 3:
          assertTrue(e.getKey().equals("B"));
          assertTrue(e.getValue().equals("y"));
          break;
        default:
          assertTrue(false);
          break;
      }
    }
  }
}
