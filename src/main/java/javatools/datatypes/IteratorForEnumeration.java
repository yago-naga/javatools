package javatools.datatypes;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

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
    this.enumerator = enumerator;
  }

  @Override
  public boolean hasNext() {
    return (enumerator.hasMoreElements());
  }

  @Override
  public T next() {
    return (enumerator.nextElement());
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("Remove on IteratirForEnumeration");
  }

  @Override
  public Iterator<T> iterator() {
    return this;
  }

  /** Returns the rest of the enumeration as a list*/
  public List<T> asList() {
    return Collections.list(enumerator);
  }

}
