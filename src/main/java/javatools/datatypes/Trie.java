package javatools.datatypes;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import javatools.administrative.D;

/**
Copyright 2016 Fabian M. Suchanek

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

The class implements the Trie data type.
 */
public class Trie extends AbstractSet<CharSequence> {

  /** Holds the children */
  protected TreeMap<Character, Trie> children = new TreeMap<Character, Trie>();

  /** true if this is a word */
  protected boolean isWord = false;

  /** number of elements */
  protected int size = 0;

  /** maps to parent*/
  protected Trie parent;

  /** Constructs a Trie*/
  public Trie() {

  }

  /** Constructs a Trie*/
  protected Trie(Trie p) {
    parent = p;
  }

  /** Constructs a trie*/
  public Trie(Collection<String> keySet) {
    for (String s : keySet)
      add(s);
  }

  @Override
  public boolean add(CharSequence s) {
    boolean a = add(s, 0);
    if (a) size++;
    return (a);
  }

  @Override
  public void clear() {
    children.clear();
    isWord = false;
    size = 0;
  }

  @Override
  public boolean isEmpty() {
    return (size == 0);
  }

  /** Adds a sequence starting from start position */
  protected boolean add(CharSequence s, int start) {
    if (s.length() == start) {
      if (isWord) return (false);
      isWord = true;
      return (true);
    }
    Character c = s.charAt(start);
    if (children.get(c) == null) children.put(c, new Trie(this));
    return (children.get(c).add(s, start + 1));
  }

  @Override
  public boolean contains(Object s) {
    return (s instanceof CharSequence && containsCS((CharSequence) s, 0));
  }

  /** TRUE if the trie contains the sequence from start position on */
  protected boolean containsCS(CharSequence cs, int start) {
    if (cs.length() == start) return (isWord);
    Character c = cs.charAt(start);
    if (children.get(c) == null) return (false);
    return (children.get(c).containsCS(cs, start + 1));
  }

  @Override
  public PeekIterator<CharSequence> iterator() {
    if (isEmpty()) return (PeekIterator.emptyIterator());
    return (new PeekIterator<CharSequence>() {

      StringBuilder currentString = new StringBuilder();

      Trie currentTrie = Trie.this;

      @Override
      protected CharSequence internalNext() throws Exception {
        do {
          SortedMap<Character, Trie> chars = currentTrie.children;
          // If we cannot go down
          while (chars.isEmpty()) {
            // If we cannot go up, we give up
            if (currentTrie.parent == null) return (null);
            // Go up
            currentTrie = currentTrie.parent;
            // Take next neighbor
            chars = currentTrie.children.headMap(currentString.charAt(currentString.length() - 1));
            currentString.setLength(currentString.length() - 1);
          }
          // Finally, we can go down
          Character c = chars.lastKey();
          currentString.append(c.charValue());
          currentTrie = currentTrie.children.get(c);
        } while (!currentTrie.isWord);
        return (currentString.toString());
      }

    });
  }

  @Override
  public String toString() {
    return "Trie with " + size() + " elements and " + children.size() + " children";
  }

  @Override
  public int size() {
    return (size);
  }

  /**
   * Returns the length of the longest contained subsequence, starting from
   * start position
   */
  public int containedLength(CharSequence s, int startPos) {
    int terminationValue = (isWord ? 0 : -1);
    if (s.length() <= startPos) return terminationValue;
    Character c = s.charAt(startPos);
    if (children.get(c) == null) return terminationValue;
    int subtreelength = children.get(c).containedLength(s, startPos + 1);
    if (subtreelength == -1) return terminationValue;
    return (subtreelength + 1);
  }

  /** Returns all words found */
  public PeekIterator<CharSequence> wordsIn(final CharSequence text) {
    return (new PeekIterator<CharSequence>() {

      int pos = -1;

      @Override
      public CharSequence internalNext() {
        while (++pos < text.length()) {
          int subtreeLength = containedLength(text, pos);
          if (subtreeLength != -1) return (text.subSequence(pos, subtreeLength + pos));
        }
        return (null);
      }
    });
  }

  /** Test method */
  public static void main(String[] args) {
    Trie t = new Trie();
    t.add("hallo");
    t.add("du");
    t.add("duplizieren");
    for (String s : new IterableForIterator<String>(t.stringIterator()))
      D.p(s);
    D.p(t.wordsIn("Blah hallo blub hallo fasel du duplizieren").asList());
  }

  public Iterator<String> stringIterator() {
    return (new MappedIterator<CharSequence, String>(iterator(), MappedIterator.stringMapper));
  }

  public Iterable<String> strings() {
    return (new MappedIterator<CharSequence, String>(iterator(), MappedIterator.stringMapper));
  }
}
