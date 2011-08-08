package javatools.datatypes;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javatools.parsers.DateParser;

import org.junit.Test;

public class DateParserTest {

	@Test
	public void testNormalize() {
		assertEquals("-26-##-##", DateParser.normalize("26 BC"));

	}

	@Test
	public void testGetDates() {
		List<String> dates = DateParser.getDates("the dog was born in -26-##-##");
		assertEquals(1, dates.size());
		assertEquals("-26-##-##", dates.get(0));
		dates = DateParser.getDates("the dog was born in 26-##-##");
		assertEquals(1, dates.size());
		assertEquals("26-##-##", dates.get(0));
	}
}
