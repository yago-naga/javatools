package javatools.datatypes;

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


This class provides the simple datatype of a pair */
public class Pair<F, S> implements Comparable<Pair<F, S>> {

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
    this.first = first;
  }

  /** Returns the second */
  public S second() {
    return second;
  }

  /** Sets the second */
  public void setSecond(S second) {
    this.second = second;
  }

  /** Constructs a Pair*/
  public Pair(F first, S second) {
    super();
    this.first = first;
    this.second = second;
  }

  /** Constructs an empty pair */
  public Pair() {
    super();
  }

  @Override
  public int hashCode() {
    return (first.hashCode() ^ second.hashCode());
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Pair && ((Pair<?, ?>) obj).first.equals(first) && ((Pair<?, ?>) obj).second.equals(second);
  }

  /** Returns "first/second"*/
  @Override
  public String toString() {
    return first + "/" + second;
  }

  @Override
  @SuppressWarnings("unchecked")
  public int compareTo(Pair<F, S> o) {
    int firstCompared = ((Comparable<F>) first).compareTo(o.first());
    if (firstCompared != 0) return (firstCompared);
    return (((Comparable<S>) second).compareTo(o.second()));
  }
}
