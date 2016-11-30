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

  
This class implements an undirected graph.
*/

public class UndirectedGraph<E extends Comparable<E>> extends DirectedGraph<E> {

  public UndirectedGraph() {
    super();
  }

  /** Returns a node or creates it*/
  @Override
  public Node<E> getOrMake(E label) {
    if (!nodes.containsKey(label)) nodes.put(label, new Node<E>(label, false));
    return (nodes.get(label));
  }

}
