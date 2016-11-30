package javatools.datatypes;

import java.util.AbstractList;
import java.util.AbstractSet;
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



 This class provides a wrapper for immutable lists and sets  
 */
public class Immutable {

  public static class List<E> extends AbstractList<E> {

    protected java.util.List<E> list;

    public List(java.util.List<E> l) {
      list = l;
    }

    @Override
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
      set = s;
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
