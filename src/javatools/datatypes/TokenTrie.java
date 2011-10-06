package javatools.datatypes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Java Tools (see
 * http://mpii.de/yago-naga/javatools). It is licensed under the Creative
 * Commons Attribution License (see http://creativecommons.org/licenses/by/3.0)
 * by the YAGO-NAGA team (see http://mpii.de/yago-naga).
 * 
 * Stores phrases in a tree structure to
 * allow efficient access. 
 * @author Johannes Hoffart
 *
 */
public class TokenTrie
{
  private TokenTrieElement root;
  
  public TokenTrie() {
    root = new TokenTrieElement(null);
  }
  
  /**
   * Add a phrase, each array entry is a phrase token
   * 
   * @param phraseParts
   */
  public void addPhrase(String[] phraseParts) {
    TokenTrieElement current = root;
    
    for (String phrasePart : phraseParts) {
      TokenTrieElement child = current.addChild(phrasePart);
      current = child;
    }
    
    current.setEndElement(true);
  }

  /**
   * Checks if the phrase is contained in the tree
   * 
   * @param phraseParts Phrase as token array
   * @return        true if contained, false otherwise
   */
  public boolean contains(String[] phraseParts) {
    int i=0;
    
    TokenTrieElement match = root.getChild(phraseParts[i]);
    
    while (match != null && i < phraseParts.length-1) {
      i++;
      
      String childToken = phraseParts[i];
      TokenTrieElement child = match.getChild(childToken);
      match = child;
    }
    
    if (match != null) {
      // make sure that the phrase is only matched fully
      return match.isEndElement();
    } else {
      return false;
    }
  }
  
  /**
   * Returns the longest matching phrase in the tree, beginning
   * with the first array entry in matchText.
   * 
   * @param matchText Text to match against, pre-tokenized
   * @return      Longest matching phrase stored in the tree, as token-array
   */
  public String[] getLongestMatch(String[] matchText) {
    int i=0;
    
    TokenTrieElement match = root.getChild(matchText[i]);
    
    boolean isEndElement = false;
    
    while (match != null && i < matchText.length-1) {
      isEndElement = match.isEndElement();
    
      i++;
      
      String childToken = matchText[i];
      TokenTrieElement child = match.getChild(childToken);
      match = child;
    }
      
    String[] matchedString = null;
    
    if (isEndElement) {
      matchedString = Arrays.copyOfRange(matchText, 0, i);
    }
    
    return matchedString;
  }
  
  private class TokenTrieElement
  {    
    private boolean endElement;
    
    private Map<String,TokenTrieElement> children;
    
    public TokenTrieElement(String word) {     
      children = new HashMap<String,TokenTrieElement>();
    }
        
    public TokenTrieElement addChild(String word) {
      // do not add if it exists
      TokenTrieElement child = getChild(word);
      
      if (child == null) {
        child = new TokenTrieElement(word);
        children.put(word,child);
      }
      
      return child;
    }
    
    public TokenTrieElement getChild(String word) {
      return children.get(word);
    }

    public boolean isEndElement()
    {
      return endElement;
    }

    public void setEndElement(boolean endElement)
    {
      this.endElement = endElement;
    }
  }
}

