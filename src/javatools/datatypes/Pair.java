package javatools.datatypes;

/** 
This class is part of the Java Tools (see http://mpii.de/~suchanek/downloads/javatools).
It is licensed under the Creative Commons Attribution License 
(see http://creativecommons.org/licenses/by/3.0) by 
Fabian M. Suchanek (see http://mpii.de/~suchanek).
  
If you use the class for scientific purposes, please cite our paper
  Fabian M. Suchanek, Georgiana Ifrim and Gerhard Weikum
  "Combining Linguistic and Statistical Analysis to Extract Relations from Web Documents" (SIGKDD 2006)

This class provides the simple datatype of a pair */ 
public class Pair<F,S> implements Comparable<Pair<F,S>> {
  /** Holds the first component */
  public F first;
  /** Holds the second component */  
  public S second;
  /** Returns the first */
  public F first() {
    return first;
  }
  /** Sets the first */
  public void setFirst(F first) {
    this.first=first;
  }
  /** Returns the second */
  public S second() {
    return second;
  }
  /** Sets the second */
  public void setSecond(S second) {
    this.second=second;
  }
  
  /** Constructs a Pair*/
  public Pair(F first, S second) {
    super();
    this.first=first;
    this.second=second;
  }  

  public int hashCode() {
    return(first.hashCode()^second.hashCode());
  }
  
  @Override
  public boolean equals(Object obj) {   
    return obj instanceof Pair && ((Pair)obj).first.equals(first) && ((Pair)obj).second.equals(second);
  }
  /** Returns "first/second"*/
  public String toString() {
    return first+"/"+second;
  }
  
  @SuppressWarnings("unchecked")
  public int compareTo(Pair<F, S> o) {
    int firstCompared=((Comparable<F>)first).compareTo(o.first());
    if(firstCompared!=0) return(firstCompared);
    return(((Comparable<S>)second).compareTo(o.second()));
  }
}
