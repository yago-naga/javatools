package javatools.datatypes;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TokenTrieTest
{
	private TokenTrie phrases;
	
	@Before
	public void setUp()
		throws Exception
	{
		phrases = new TokenTrie();
		
		phrases.addPhrase("the red dog".split(" "));
		phrases.addPhrase("the red".split(" "));
		phrases.addPhrase("the new kid".split(" "));
		phrases.addPhrase("a".split(" "));
	}
	
	@Test
	public void containsTest()
		throws Exception
	{
		assertFalse(phrases.contains("the".split(" ")));
		assertFalse(phrases.contains("the new".split(" ")));
		assertFalse(phrases.contains("the new BUNNY".split(" ")));
		assertFalse(phrases.contains("the red dog barks".split(" ")));
		assertTrue(phrases.contains("a".split(" ")));
		assertTrue(phrases.contains("the red dog".split(" ")));
		assertTrue(phrases.contains("the red".split(" ")));
		assertTrue(phrases.contains("the new kid".split(" ")));
	}
	
	@Test
	public void matchTest()
		throws Exception
	{
		String[] sentence = "the red dog whines".split(" ");
		String[] longestMatch = phrases.getLongestMatch(sentence);
		
		assertArrayEquals(longestMatch, "the red dog".split(" "));
		
		sentence = "the".split(" ");
		assertNull(phrases.getLongestMatch(sentence));
		
		sentence = "red dog".split(" ");
		assertNull(phrases.getLongestMatch(sentence));
		
		sentence = "the new".split(" ");
		assertNull(phrases.getLongestMatch(sentence));
	}
}
