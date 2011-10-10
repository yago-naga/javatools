package javatools.datatypes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class PhraseDictionaryTest {

  private PhraseDictionary datg;

  @Before
  public void setUp() throws Exception {
    datg = new PhraseDictionary(new File("testdata/test_phrases.txt"));

  }

  @Test
  public void containsTest() throws Exception {
    assertFalse(datg.contains(new String[] { "IMNOTTHERE" }));
    assertFalse(datg.contains("the".split(" ")));
    assertFalse(datg.contains("the new".split(" ")));
    assertFalse(datg.contains("the new BUNNY".split(" ")));
    assertFalse(datg.contains("the red dog barks".split(" ")));
    assertFalse(datg.contains("red dog".split(" ")));
    assertTrue(datg.contains("a".split(" ")));
    assertTrue(datg.contains("the red dog".split(" ")));
    assertTrue(datg.contains("the red".split(" ")));
    assertTrue(datg.contains("the new kid".split(" ")));
  }

  @Test
  public void matchTest() throws Exception {
    String[] sentence = "here the red dog whines".split(" ");
    String[] longestMatch = datg.getLongestMatch(sentence, 1);
    assertArrayEquals(longestMatch, "the red dog".split(" "));

    sentence = "the red dog whines".split(" ");
    longestMatch = datg.getLongestMatch(sentence);

    assertArrayEquals(longestMatch, "the red dog".split(" "));

    sentence = "the".split(" ");
    assertArrayEquals(new String[0], datg.getLongestMatch(sentence));

    sentence = "red dog".split(" ");
    assertArrayEquals(new String[0], datg.getLongestMatch(sentence));

    sentence = "the new".split(" ");
    assertArrayEquals(new String[0], datg.getLongestMatch(sentence));
    
    sentence = "IMNOTTHERE".split(" ");
    assertArrayEquals(new String[0], datg.getLongestMatch(sentence));
  }
}
