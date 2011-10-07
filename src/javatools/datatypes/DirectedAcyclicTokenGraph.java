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

public class DirectedAcyclicTokenGraph {

  private int[][] adj;

  private Set<Integer> startNodes;
  private Map<String, Integer> token2id;
  private Map<Integer, String> id2token;

  private static final int EOW = Integer.MAX_VALUE;

  public DirectedAcyclicTokenGraph(File phraseFile) throws IOException {
    startNodes = new HashSet<Integer>();
    token2id = new HashMap<String, Integer>();
    id2token = new HashMap<Integer, String>();

    // first build an intermediate structure
    Map<Integer, Set<Integer>> g = createTemporaryGraph(phraseFile);

    // transform the temporary structure into a memory-efficient structure
    adj = createFinalGraph(g);
  }

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

  public String[] getLongestMatch(String[] sentence) {
    if (sentence == null || sentence.length == 0) {
      return sentence;
    }
  
    List<Integer> longestMatch = new LinkedList<Integer>();
        
    Integer id = getIdForToken(sentence[0], false);
    
    int[] successors = null;
    
    if (id != null && startNodes.contains(id)) {

      if (id != null) {
        longestMatch.add(id);
        successors = adj[id];
      }

      for (int i = 1; i < sentence.length; i++) {
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
      phrase[i++] = id2token.get(id);
    }
    
    return phrase;
  }

  private Map<Integer, Set<Integer>> createTemporaryGraph(File phraseFile) throws IOException {
    Map<Integer, Set<Integer>> g = new HashMap<Integer, Set<Integer>>();

    for (String line : new FileLines(phraseFile, "Building directed acyclic token graph")) {
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
      id2token.put(id, token);
    }

    return id;
  }

}
