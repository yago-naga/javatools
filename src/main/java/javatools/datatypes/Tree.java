package javatools.datatypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

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


   The class represents the simple datatype of a Tree.*/
public class Tree<T> implements Iterable<T>, Visitable<Tree<T>> {

  /** Holds the children */
  protected List<Tree<T>> children = new ArrayList<Tree<T>>();;

  /** Holds the node */
  protected T element;

  /** Points to the parent*/
  protected Tree<T> parent;

  /** Constructs an empty tree with a null element*/
  public Tree() {
    this(null);
  }

  /** Constructs a tree with a node element*/
  public Tree(T element) {
    this(null, element);
  }

  /** Constructs a tree with a node element and a parent*/
  public Tree(Tree<T> parent, T element) {
    this(parent, element, new ArrayList<Tree<T>>(0));
  }

  /** Constructs a tree with a node element and children*/
  public Tree(T element, List<Tree<T>> children) {
    this(null, element, children);
  }

  /** Constructs a tree with a node element and a parent*/
  public Tree(Tree<T> parent, T element, List<Tree<T>> children) {
    setParent(parent);
    setElement(element);
    setChildren(children);
  }

  /** Returns the parent */
  public Tree<T> getParent() {
    return parent;
  }

  /** Sets the parent */
  public void setParent(Tree<T> parent) {
    this.parent = parent;
  }

  /** Depth first search across the tree*/
  public List<Tree<T>> getChildren() {
    return children;
  }

  /** Sets the children */
  public void setChildren(List<Tree<T>> children) {
    children = new ArrayList<Tree<T>>();
    for (Tree<T> child : children)
      addChild(child);
  }

  /** Adds a child */
  public void addChild(Tree<T> child) {
    children.add(child);
    child.parent = this;
  }

  /** Returns the element */
  public T getElement() {
    return element;
  }

  /** Sets the element */
  public void setElement(T element) {
    this.element = element;
  }

  @Override
  public Iterator<T> iterator() {
    return (new Iterator<T>() {

      Stack<Pair<Tree<T>, Integer>> dfs = new Stack<Pair<Tree<T>, Integer>>();

      {
        dfs.push(new Pair<Tree<T>, Integer>(Tree.this, -1));
      }

      protected void prepare() {
        if (dfs.size() == 0) return;
        if (dfs.peek().first().children.size() > dfs.peek().second()) return;
        dfs.pop();
        prepare();
      }

      @Override
      public boolean hasNext() {
        prepare();
        return (dfs.size() != 0);
      }

      @Override
      public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        int childNum = dfs.peek().second();
        dfs.peek().setSecond(childNum + 1);
        if (childNum == -1) {
          return (dfs.peek().first().getElement());
        }
        dfs.push(new Pair<Tree<T>, Integer>(dfs.peek().first().getChildren().get(childNum), -1));
        return next();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

    });
  }

  /** Receives a visitor for a depth first traversal */
  @Override
  public boolean receive(Visitor<Tree<T>> visitor) throws Exception {
    if (!visitor.visit(this)) return (false);
    for (Tree<T> child : children) {
      if (!child.receive(visitor)) return (false);
    }
    return (true);
  }

  @Override
  public String toString() {
    if (children.size() == 0) return element.toString();
    return element.toString() + children;
  }

  /**
   * Prints a tree with visualization of its structure.
   *
   * Example:
   *   root
   *   +-root.0
   *   | +-root.0.0
   *   | +-root.0.1
   *   +-root.1
   *     +-root.1.0
   *     +-root.1.1
   */
  public String toPrettyString() {
    StringBuilder sb = new StringBuilder();
    prettyPrint(sb, null, false);
    return sb.toString();
  }

  /**
   * Helper function for toPrettyString.
   * @param stringBuilder which gets the output
   * @param prefix is added in front of every line
   * @param parentIsLastChild determine whether line should be continued
   */
  private void prettyPrint(StringBuilder stringBuilder, String prefix, boolean parentIsLastChild) {
    // print actual element
    stringBuilder.append((prefix == null ? "" : (prefix + "+-")));
    stringBuilder.append(getElement());
    stringBuilder.append("\n");

    // print children
    List<Tree<T>> list = getChildren();
    int i = 0;
    for (Tree<T> child : list) {
      // don't continue line if it's the last in the list
      String addToPrefix = parentIsLastChild ? "  " : "| ";
      child.prettyPrint(stringBuilder, prefix == null ? "" : prefix + addToPrefix, i == list.size() - 1);
      i++;
    }
  }

  public static void main(String[] args) {
    Tree<Integer> t1 = new Tree<Integer>(1);
    Tree<Integer> t2 = new Tree<Integer>(2);
    Tree<Integer> t3 = new Tree<Integer>(3);
    Tree<Integer> a1 = new Tree<Integer>(-1);
    Tree<Integer> t0 = new Tree<Integer>(0);
    t0.addChild(a1);
    t0.addChild(t1);
    t1.addChild(t2);
    t2.addChild(t3);
    for (Integer i : t0) {
      D.p(i);
    }
    D.p(t0);
  }
}
