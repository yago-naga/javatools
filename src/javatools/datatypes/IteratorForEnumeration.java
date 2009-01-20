package javatools.datatypes;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/** 
This class is part of the Java Tools (see http://mpii.de/~suchanek/downloads/javatools).
It is licensed under the Creative Commons Attribution License 
(see http://creativecommons.org/licenses/by/3.0) by 
Fabian M. Suchanek (see http://mpii.de/~suchanek).
  
If you use the class for scientific purposes, please cite our paper
  Fabian M. Suchanek, Georgiana Ifrim and Gerhard Weikum
  "Combining Linguistic and Statistical Analysis to Extract Relations from Web Documents" (SIGKDD 2006)

  This class provides a simple converter from Enumerations to Iterators.
  It accepts untyped Enumerations, but yields always typed Iterators.
  It can also convert an untyped Enumeration to a list.<BR>
  Example:<BR>
  <PRE>
    for(String s : new IteratorForEnumeration&lt;String>(someUntypedEnumeration)) {
      System.out.println(s);
    }
  </PRE>
  */
public class IteratorForEnumeration<T> implements Iterator<T>, Iterable<T> {

  /** Holds the enumeration object*/
  public Enumeration<T> enumerator;
    
  @SuppressWarnings("unchecked")
  public IteratorForEnumeration(Enumeration enumerator) {   
    this.enumerator=enumerator;
  }

  public boolean hasNext() {
    return(enumerator.hasMoreElements());
  }

  public T next() {
    return(enumerator.nextElement());
  }

  public void remove() {
   throw new UnsupportedOperationException("Remove on IteratirForEnumeration");
  }

  public Iterator<T> iterator() {    
    return this;
  }
  
  /** Returns the rest of the enumeration as a list*/
  public List<T> asList() {
    return Collections.list(enumerator);
  }

}
