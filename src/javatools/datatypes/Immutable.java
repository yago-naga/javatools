package javatools.datatypes;

import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.Iterator;

/** 
 This class is part of the Java Tools (see http://mpii.de/~suchanek/downloads/javatools).
 It is licensed under the Creative Commons Attribution License 
 (see http://creativecommons.org/licenses/by/3.0) by 
 Fabian M. Suchanek (see http://mpii.de/~suchanek).
 
 If you use the class for scientific purposes, please cite our paper
 Fabian M. Suchanek, Georgiana Ifrim and Gerhard Weikum
 "Combining Linguistic and Statistical Analysis to Extract Relations from Web Documents" (SIGKDD 2006)

 This class provides a wrapper for immutable lists and sets  
 */
public class Immutable {

  public static class List<E> extends AbstractList<E> {

    protected java.util.List<E> list;

    public List(java.util.List<E> l) {
      list = l;
    }

    public E get(int index) {
      return list.get(index);
    }

    @Override
    public int size() {
      return list.size();
    }
  }
  
  public static class Set<E> extends AbstractSet<E> {
    protected java.util.Set<E> set;
    public Set(java.util.Set<E> s) {
      set=s;
    }
    @Override
    public Iterator<E> iterator() {      
      return set.iterator();
    }

    @Override
    public int size() {
      return set.size();
    }
    
  }
}
