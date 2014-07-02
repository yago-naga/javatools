package javatools.parsers;

import static org.junit.Assert.*;

import org.junit.Test;


public class CharTest {

  @Test
  public void testDecodeAmpersand() {
    String s = "This is &amp;";
    String sDec = Char17.decodeAmpersand(s);
    assertEquals("This is &", sDec);
    
    s = "This&amp;is";
    sDec = Char17.decodeAmpersand(s);
    assertEquals("This&is", sDec);
    
    s = "&amp;This is";
    sDec = Char17.decodeAmpersand(s);
    assertEquals("&This is", sDec);
    
    s = "This is";
    sDec = Char17.decodeAmpersand(s);
    assertEquals("This is", sDec);
  }
}
