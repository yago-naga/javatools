package javatools.datatypes;

import static org.junit.Assert.*;
import javatools.parsers.DateParser;

import org.junit.Test;


public class DateParserTest {

  @Test
  public void testNormalize() {
    assertEquals("-26-##-##",DateParser.normalize("26 BC"));
  }
  
  public void testGetDates() {
    
  }
}
