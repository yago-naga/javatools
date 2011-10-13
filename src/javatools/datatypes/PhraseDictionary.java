package javatools.datatypes;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javatools.filehandlers.FileLines;

/**
 * This class is part of the Java Tools (see http://mpii.de/yago-naga/javatools).
 * It is licensed under the Creative Commons Attribution License 
 * (see http://creativecommons.org/licenses/by/3.0) by 
 * the YAGO-NAGA team (see http://mpii.de/yago-naga).
 * 
 * Memory-Efficient datastructure to store a phrase dictionary.
 * Supports a contains and a getLongestMatch operation, which
 * should be sufficient to annotate phrases in a text.
 * 
 * It takes an input file that needs to contain pre-tokenized phrases.
 * A space (' ') is taken as a token separator on reading.
 * 
 * The input is a file so that the strings can be processed immediately,
 * without having to create an in-memory datastructure to pass the strings first.
 * 
 * @author Johannes Hoffart
 *
 */
public class PhraseDictionary {

  private int[][] adj;

  private Set<Integer> startNodes;
  private Map<String, Integer> token2id;
  private String[] id2token;
  
  private int minimumPhraseLengthInCharacters = 0;

  private static final int EOW = Integer.MAX_VALUE;

  /**
   * Builds a phrase dictionary from the given file.
   * 
   * @param phraseFile  File containing pre-tokenized phrases. Token delimiter is a space (' ')
   * @throws IOException
   */
  public PhraseDictionary(File phraseFile) throws IOException {
    new PhraseDictionary(phraseFile, 0);
  }
  
  /**
   * Builds a phrase dictionary from the given file.
   * 
   * @param phraseFile  File containing pre-tokenized phrases. Token delimiter is a space (' ')
   * @param minPhraseCharLength All phrases shorter than this (in characters) will be skipped
   * @throws IOException
   */
  public PhraseDictionary(File phraseFile, int minPhraseCharLength) throws IOException {
    this.minimumPhraseLengthInCharacters = minPhraseCharLength;
    
    startNodes = new HashSet<Integer>();
    token2id = new HashMap<String, Integer>();
    
    // first build an intermediate structure
    Map<Integer, Set<Integer>> g = createTemporaryGraph(phraseFile);

    // transform the temporary structure into a memory-efficient structure
    adj = createFinalGraph(g);
    g = null;
  }
  
  /**
   * Checks if the dictionary contains the given phrase
   * 
   * @param phrase  Phrase to check for in the dictionary
   * @return        true if contained, false otherwise
   */
  public boolean contains(String[] phrase) {
    if (phrase == null || phrase.length == 0) {
      return false;
    }
    
    Integer[] pIds = getPhraseAsIds(phrase, false);
    
    if (pIds == null || !startNodes.contains(pIds[0])) {
      return false;
    }
    
    int[] successors = adj[pIds[0]];
    
    for (int i=1; i<phrase.length; i++) {
      int tokenId = pIds[i];
      
      if (Arrays.binarySearch(successors, tokenId) >= 0) {
        successors = adj[tokenId];
      } else {
        successors = null;
        break;
      }
    }
    
    boolean contains = false;
    
    if (successors != null) {
      contains = (Arrays.binarySearch(successors, EOW) >= 0);
    }
    
    return contains;
  }
  
  /**
   * Returns the longest matching phrase in the given sentence.
   * The first matching token is taken to be the starting point
   * of the longest match.
   * 
   * @param sentence  Sentence to match against prhase dictionary
   * @return          Longest phrase matching the sentence.
   */
  public String[] getLongestMatch(String[] sentence) {
    return getLongestMatch(sentence, 0);
  }

  /**
   * Returns the longest matching phrase in the given sentence,
   * starting at the start offset.
   * 
   * The first matching token is taken to be the starting point
   * of the longest match.
   * 
   * @param sentence  Sentence to match against prhase dictionary
   * @param start     Start the matching in the sentence from here
   * @return          Longest phrase matching the sentence.
   */
  public String[] getLongestMatch(String[] sentence, int start) {
    if (sentence == null || (sentence.length - start) == 0) {
      return sentence;
    }
  
    List<Integer> longestMatch = new LinkedList<Integer>();
        
    Integer id = getIdForToken(sentence[start], false);
    
    int[] successors = null;
    
    if (id != null && startNodes.contains(id)) {

      if (id != null) {
        longestMatch.add(id);
        successors = adj[id];
      }

      for (int i = start+1; i < sentence.length; i++) {
        Integer tokenId = getIdForToken(sentence[i], false);

        if (tokenId == null) {
          break;
        } else {
          if (Arrays.binarySearch(successors, tokenId) >= 0) {
            longestMatch.add(tokenId);
            successors = adj[tokenId];
          } else {
            break;
          }
        }
      }
    }
    
    boolean contains = false;
    
    if (successors != null) {
      contains = (Arrays.binarySearch(successors, EOW) >= 0);
    }
    
    if (contains) {
      return getIdsAsPhrase(longestMatch);
    } else {
      return new String[0];
    }
  }

  private String[] getIdsAsPhrase(List<Integer> phraseIds) {
    String[] phrase = new String[phraseIds.size()];
    
    int i=0;
    
    for (Integer id : phraseIds) {
      phrase[i++] = id2token[id];
    }
    
    return phrase;
  }

  private Map<Integer, Set<Integer>> createTemporaryGraph(File phraseFile) throws IOException {
    Map<Integer, Set<Integer>> g = new HashMap<Integer, Set<Integer>>();

    for (String line : new FileLines(phraseFile, "Building phrase dictionary")) {
      if (line.length() < minimumPhraseLengthInCharacters) {
        continue;
      }
      
      String[] p = line.split(" ");
      assert (p.length > 0);

      Integer[] pId = getPhraseAsIds(p, true);

      startNodes.add(pId[0]);      
      Set<Integer> successors = getSuccessors(g, pId[0]);

      for (int i = 1; i < pId.length; i++) {
        int currentTokenId = pId[i];

        successors.add(currentTokenId);
        successors = getSuccessors(g, currentTokenId);
      }

      successors.add(EOW);
    }
    
    // create reverse id-to-token map
    id2token = new String[token2id.size()];
    
    for (Entry<String, Integer> tokenId : token2id.entrySet()) {
      id2token[tokenId.getValue()] = tokenId.getKey();
    }

    return g;
  }
  
  private int[][] createFinalGraph(Map<Integer, Set<Integer>> g) {
    int[][] f = new int[g.size()][];

    for (Entry<Integer, Set<Integer>> entry : g.entrySet()) {
      int[] sortedSuccessors = getSortedSuccessors(entry.getValue());

      f[entry.getKey()] = sortedSuccessors;
    }

    return f;
  }

  private int[] getSortedSuccessors(Set<Integer> successors) {
    int[] successorArray = new int[successors.size()];

    int i = 0;
    for (Integer s : successors) {
      successorArray[i++] = s;
    }

    Arrays.sort(successorArray);

    return successorArray;
  }

  private Set<Integer> getSuccessors(Map<Integer, Set<Integer>> g, int i) {
    Set<Integer> s = g.get(i);

    if (s == null) {
      s = new HashSet<Integer>();
      g.put(i, s);
    }

    return s;
  }

  private Integer[] getPhraseAsIds(String[] phrase, boolean createId) {
    Integer[] ids = new Integer[phrase.length];

    int i = 0;
    for (String p : phrase) {
      Integer id = getIdForToken(p, createId);
      
      if (id == null) {
        // the token and thus the whole phrase is not present
        return null;
      }

      ids[i++] = id;
    }

    return ids;
  }

  private Integer getIdForToken(String token, boolean createId) {
    Integer id = token2id.get(token);

    if (id == null && createId) {
      id = token2id.size();
      token2id.put(token, id);
    }

    return id;
  }

}
