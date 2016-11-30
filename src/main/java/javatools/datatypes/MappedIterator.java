package javatools.datatypes;

import java.io.Closeable;
import java.io.IOException;
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


  The class wraps an iterator and translates each element before returning it  */

public class MappedIterator<S, T> implements Iterator<T>, Iterable<T>, Closeable {

  protected Iterator<S> iterator;

  protected Map<? super S, ? extends T> map;

  public static interface Map<A, B> {

    public B map(A a);
  }

  /** An iterator that maps an object to a string*/
  public static MappedIterator.Map<Object, String> stringMapper = new MappedIterator.Map<Object, String>() {

    @Override
    public String map(Object a) {
      return a.toString();
    }
  };

  public MappedIterator(Iterator<S> i, Map<? super S, ? extends T> m) {
    iterator = i;
    map = m;
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public T next() {
    return (map.map(iterator.next()));
  }

  @Override
  public void remove() {
    iterator.remove();
  }

  @Override
  public Iterator<T> iterator() {
    return this;
  }

  @Override
  public void close() throws IOException {
    if (iterator instanceof Closeable) ((Closeable) iterator).close();
  }
}
