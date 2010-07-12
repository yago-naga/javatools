package javatools.datatypes;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javatools.administrative.D;

/**
 * This class is part of the Java Tools (see
 * http://mpii.de/yago-naga/javatools). It is licensed under the Creative
 * Commons Attribution License (see http://creativecommons.org/licenses/by/3.0)
 * by the YAGO-NAGA team (see http://mpii.de/yago-naga).
 * 
 * The class implements the Trie data type.
 */
public class Trie extends AbstractSet<CharSequence> {

	/** Holds the children */
	protected Map<Character, Trie> children = new TreeMap<Character, Trie>();

	/** true if this is a word */
	protected boolean isWord = false;

	@Override
	public boolean add(CharSequence s) {
		return (add(s, 0));
	}

	/** Adds a sequence starting from start position */
	protected boolean add(CharSequence s, int start) {
		if (s.length() == start) {
			if (isWord)
				return (false);
			isWord = true;
			return (true);
		}
		Character c = s.charAt(start);
		if (children.get(c) == null)
			children.put(c, new Trie());
		return (children.get(c).add(s, start + 1));
	}

	@Override
	public boolean contains(Object s) {
		return (s instanceof CharSequence && containsCS((CharSequence) s, 0));
	}

	/** TRUE if the trie contains the sequence from start position on */
	protected boolean containsCS(CharSequence cs, int start) {
		if (cs.length() == start)
			return (isWord);
		Character c = cs.charAt(start);
		if (children.get(c) == null)
			return (false);
		return (children.get(c).containsCS(cs, start + 1));
	}

	@Override
	public Iterator<CharSequence> iterator() {
		throw new UnsupportedOperationException("Iterator for Trie");
	}

	@Override
	public int size() {
		int num = isWord ? 1 : 0;
		for (Trie child : children.values())
			num += child.size();
		return (num);
	}

	/**
	 * Returns the length of the longest contained subsequence, starting from
	 * start position
	 */
	public int containedLength(CharSequence s, int startPos) {
		if (isWord)
			return (0);
		Character c = s.charAt(startPos);
		if (children.get(c) == null)
			return (-1);
		int subtreelength = children.get(c).containedLength(s, startPos + 1);
		if (subtreelength == -1)
			return (-1);
		return (subtreelength + 1);
	}

	/** Returns all words found */
	public PeekIterator<CharSequence> wordsIn(final CharSequence text) {
		return (new PeekIterator<CharSequence>() {
			int pos = -1;

			public CharSequence internalNext() {
				while (++pos < text.length()) {
					int subtreeLength = containedLength(text, pos);
					if (subtreeLength != -1)
						return (text.subSequence(pos, subtreeLength + pos));					
				}
				return (null);
			}
		});
	}

	public static void main(String[] args) {
		Trie t = new Trie();
		t.add("hallo");
		t.add("du");
		D.p(t.wordsIn("Blah hallo blub hallo fasel du").asList());
	}
}
