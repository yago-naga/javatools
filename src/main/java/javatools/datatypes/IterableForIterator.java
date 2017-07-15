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


This class allows to use an iterator in a for-each-loop.<BR>
Example:
<PRE>
   for(String s : new Scanner("Scan this string")) {
      // Compiletime error, because Scanner is an Iterator but not Iterable
   }

   for(String s : new IterableForIterator&lt;String&gt;(new Scanner("Scan this string"))) {
     // works fine
   }

   or shorter (with automatic type deduction)
   for (String s : IterableForIterator.get(new Scanner("Scan this string"))) {
     // works fine
   }


</PRE>
 */
public class IterableForIterator<T> implements Iterable<T> {

  public Iterator<T> iterator;

  public IterableForIterator(Iterator<T> iterator) {
    this.iterator = iterator;
  }

  @Override
  public Iterator<T> iterator() {
    return iterator;
  }

  public static <E> Iterable<E> get(final Iterator<E> iterator) {
    return new IterableForIterator<E>(iterator);
  }
}
