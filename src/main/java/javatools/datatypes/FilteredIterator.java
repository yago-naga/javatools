package javatools.datatypes;

import java.util.Iterator;

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

  The class wraps an iterator and returns only those elements that fulfill a condition */

public class FilteredIterator<T> extends PeekIterator<T> {

  protected Iterator<T> iterator;

  protected If<T> condition;

  public static interface If<T> {

    public boolean condition(T a);
  }

  public FilteredIterator(Iterator<T> i, If<T> condition) {
    this(i);
    this.condition = condition;
  }

  protected FilteredIterator(Iterator<T> i) {
    iterator = i;
  }

  @Override
  public T internalNext() {
    while (iterator.hasNext()) {
      T obj = iterator.next();
      if (condition.condition(obj)) return (obj);
    }
    return (null);
  }

  @Override
  public void remove() {
    iterator.remove();
  }

  /** Wraps an iterator and skips elements that produce an exception*/
  public static class IgnoreErrors<T> extends FilteredIterator<T> {

    int numFail;

    /** Wraps the iterator, allowing a number of consecutive failures before returning hasNext()==false */
    public IgnoreErrors(Iterator<T> i, int numFailuresBeforeStop) {
      super(i);
      numFail = numFailuresBeforeStop;
    }

    @Override
    public T internalNext() {
      if (!iterator.hasNext()) return (null);
      int counter = numFail;
      while (counter-- > 0) {
        try {
          return (iterator.next());
        } catch (Exception e) {
        }
      }
      return (null);
    }
  }
}
