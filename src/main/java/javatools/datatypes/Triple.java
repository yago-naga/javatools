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
 
Represents a triple*/
public class Triple<F, S, T> extends Pair<F, S> {

  /** Holds the second component */
  public T third;

  /** Returns the second */
  public T third() {
    return third;
  }

  /** Constructs a Pair*/
  public Triple(F first, S second, T third) {
    super(first, second);
    this.third = third;
  }

  @Override
  public int hashCode() {
    return (super.hashCode() ^ third.hashCode());
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Triple) && ((Triple<?, ?, ?>) obj).first().equals(first) && ((Triple<?, ?, ?>) obj).second().equals(second)
        && ((Triple<?, ?, ?>) obj).third().equals(third);
  }

  /** Returns "first/second"*/
  @Override
  public String toString() {
    return first + "/" + second + "/" + third;
  }
}
