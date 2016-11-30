package javatools.datatypes;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import javatools.administrative.D;

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
 
  
This class implements a simple queue. The queue is not blocking (i.e. it will accept
as many elements as you give it). It is more efficient than a LinkedList. <BR>
Example:<BR>
<PRE>
    // Create a queue with some initial elements
    Queue&lt;Integer> a=new ArrayQueue&lt;Integer>(1,2,3,4,5,6,7,8);
    int counter=9;
    // Always add one new element and poll two
    while(a.size()!=0) {
      a.offer(counter++);
      D.p(a.poll());
      D.p(a.poll());      
    }
    -->
        1,2,3,...,14
</PRE>
*/
public class ArrayQueue<T> extends AbstractQueue<T> {

  /** Holds the queue elements*/
  protected List<T> data = new ArrayList<T>();

  /** Index of the first element*/
  protected int first = 0;

  /** Index of the last element*/
  protected int last = 0;

  /** Dummy blank objects used to enlarge the data array*/
  protected static Object[] blanks = new Object[10];

  @Override
  public Iterator<T> iterator() {
    if (first <= last) return data.subList(first, last).iterator();
    return (new CombinedIterator<T>(data.subList(first, data.size()).iterator(), data.subList(0, last).iterator()));
  }

  @Override
  public int size() {
    if (first <= last) return (last - first);
    return (data.size() - first + last);
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean offer(T o) {
    data.set(last, o);
    last = (last + 1) % data.size();
    if (last == first) {
      // Enlarge the array
      data.addAll(last, Arrays.asList((T[]) blanks));
      first += blanks.length;
    }
    return true;
  }

  @Override
  public T peek() {
    if (size() == 0) return (null);
    return (data.get(first));
  }

  @Override
  public T poll() {
    T result = peek();
    if (size() != 0) first = (first + 1) % data.size();
    return (result);
  }

  public ArrayQueue(T... initialData) {
    this(Arrays.asList(initialData));
  }

  public ArrayQueue(Collection<T> initialData) {
    data.add(null); // Ensure that the size is at least 1
    for (T element : initialData) {
      offer(element);
    }
  }

  public ArrayQueue(int size) {
    data = new ArrayList<T>(size + 1);
    data.add(null);
  }

  /** Test routine */
  public static void main(String[] args) {
    Queue<Integer> a = new ArrayQueue<Integer>();
    a.offer(1);
    D.p(a.peek());
    //1,2,3,4,5,6,7,8);
    int counter = 9;
    while (a.size() != 0) {
      a.offer(counter++);
      D.p(a.poll());
      D.p(a.poll());
    }
  }

}
