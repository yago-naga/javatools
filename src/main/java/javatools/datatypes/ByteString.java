package javatools.datatypes;

import java.util.Arrays;
import java.util.WeakHashMap;

import javatools.administrative.D;

/**
 * Copyright 2016 Fabian M. Suchanek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * 
 * This class represents strings with 1 byte per character. Thus, they use
 * roughly half as much space as ordinary strings -- but they also cannot store
 * all characters. ByteStrings are always unique. they can be compared with ==.
 * 
 * @author Fabian M. Suchanek
 * 
 */
public class ByteString implements CharSequence {

  /** Holds all strings */
  protected static WeakHashMap<ByteString, ByteString> values = new WeakHashMap<ByteString, ByteString>();

  /** Holds the string */
  public byte[] data;

  /** Hash code */
  protected int hashCode;

  /** is interned */
  public boolean isInterned = false;

  /** Constructor*/
  public static ByteString of(CharSequence s) {
    ByteString newOne = new ByteString(s);
    synchronized (values) {
      ByteString canonic = values.get(newOne);
      if (canonic != null) return (canonic);
      values.put(newOne, newOne);
      newOne.isInterned = true;
      /* We need this flag, because if we go directly always by ==, then WeakHashMap will not be able to detect if the String is already there...*/
    }
    return (newOne);
  }

  /** Use subSequence()*/
  protected ByteString(ByteString s, int start, int end) {
    data = Arrays.copyOfRange(s.data, start, end);
    hashCode = Arrays.hashCode(data);
  }

  /** Use of() */
  protected ByteString(CharSequence s) {
    data = new byte[s.length()];
    for (int i = 0; i < s.length(); i++) {
      data[i] = (byte) (s.charAt(i) - 128);
    }
    hashCode = Arrays.hashCode(data);
  }

  @Override
  public char charAt(int arg0) {
    return (char) (data[arg0] + 128);
  }

  @Override
  public int length() {
    return data.length;
  }

  @Override
  public CharSequence subSequence(int arg0, int arg1) {
    return new ByteString(this, arg0, arg1);
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ByteString)) return (false);
    ByteString other = (ByteString) obj;
    if (this.isInterned && other.isInterned) return (this == other);
    return (Arrays.equals(other.data, data));
  }

  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();
    for (int i = 0; i < length(); i++) {
      b.append(charAt(i));
    }
    return b.toString();
  }

  public static void main(String[] args) throws Exception {
    D.p(new ByteString("Hallo d�!"));
  }
}
