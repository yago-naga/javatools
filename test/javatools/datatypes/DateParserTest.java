package javatools.datatypes;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javatools.parsers.DateParser;

import org.junit.Test;

public class DateParserTest {

  @Test
  public void testNormalize() {
    assertEquals("-26-##-##", DateParser.normalize("26 BC"));
    assertEquals("##-08-23 (53 years old)", DateParser.normalize("August 23 (53 years old)"));
    assertEquals("{{death date and age| 1938-02-19|1877-02-01|df=y}}", DateParser.normalize("{{death date and age| 1938 |2|19|1877|2|1|df=y}}"));
    assertEquals("{{Birth date|1896-02-19|df=y}}",DateParser.normalize("{{Birth date|1896|February|19|df=y}}"));
  }

  @Test
  public void testGetDate() {
    String[] dates = DateParser.getDate("-26-##-##");
    assertEquals("-26", dates[0]);
    dates = DateParser.getDate("26-##-##");
    assertEquals("26", dates[0]);
  }

  @Test
  public void testGetDates() {
    List<String> dates = DateParser.getDates("the dog was born in -26-##-##");
    assertEquals(1, dates.size());
    assertEquals("-26-##-##", dates.get(0));
    dates = DateParser.getDates("the dog was born in 26-##-##");
    assertEquals(1, dates.size());
    assertEquals("26-##-##", dates.get(0));
    dates = DateParser.getDates("the dog was born in |26-##-##");
    assertEquals(1, dates.size());
    assertEquals("26-##-##", dates.get(0));
    dates = DateParser.getDates("the dog was born in (26-##-##)");
    assertEquals(1, dates.size());
    assertEquals("26-##-##", dates.get(0));
    dates = DateParser.getDates("the dog was born in )26-##-##");
    assertEquals(1, dates.size());
    assertEquals("26-##-##", dates.get(0));
    dates = DateParser.getDates("{{Death date and age|1985-95-02|1913-12-10|df=y}}");
    assertEquals(2, dates.size());
    assertEquals("1985-##-02", dates.get(0));
    assertEquals("1913-12-10", dates.get(1));
  }

}
