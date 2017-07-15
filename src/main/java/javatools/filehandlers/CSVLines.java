package javatools.filehandlers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javatools.datatypes.PeekIterator;
import javatools.parsers.Char17;

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


  The class provides an iterator over the lines in a comma-separated file<BR>
  Example:
  <PRE>
      for(List&lt;String&gt; columns : new CSVFile("c:\\autoexec.csv")) {
         System.out.println(columns);
      }
  </PRE>
  Recognizes column headers if introduced by '#'. Recognizes all types of encodings (see Char.decode()).
*/

public class CSVLines extends PeekIterator<List<String>> {

  /** number of chars for announce (or -1) */
  protected long announceChars = -1;

  /** Containes the Reader */
  protected Reader in;

  /** Holds the column names*/
  protected List<String> columns = null;

  /** Holds the next char in line */
  protected int nextChar;

  /** Holds the spearator */
  protected char separator = ',';

  /** Constructs a CSVReader*/
  public CSVLines(File f) throws IOException {
    this(new FileReader(f));
  }

  /** Constructs a CSVReader*/
  public CSVLines(Reader reader) throws IOException {
    in = reader;
    nextChar = in.read();
    if (nextChar == '/') nextChar = in.read();
    if (nextChar == '#' || nextChar == '%' || nextChar == '/') {
      nextChar = in.read();
      columns = next();
    }
  }

  /** Constructs a CSVReader*/
  public CSVLines(String file) throws IOException {
    this(new File(file));
  }

  /** Sets the separator (comma by default)*/
  public void setSeparator(char s) {
    separator = s;
  }

  /** Reads a component and the following comma*/
  protected String component() throws IOException {
    StringBuilder component = new StringBuilder();
    // Skip whitespace
    while (Character.isWhitespace(nextChar)) {
      nextChar = in.read();
    }
    // For quoted components...
    if (nextChar == '"') {
      nextChar = in.read();
      while (nextChar != -1) {
        if (nextChar == '"') {
          nextChar = in.read();
          if (nextChar != '"') break;
        }
        component.append((char) nextChar);
        nextChar = in.read();
      }
      // Skip following whitespace
      while (nextChar != 10 && nextChar != 13 && nextChar != -1 && Character.isWhitespace(nextChar)) {
        nextChar = in.read();
      }
    } else {
      // For unquoted components
      while (nextChar != -1 && nextChar != separator && nextChar != 10 && nextChar != 13) {
        component.append((char) nextChar);
        nextChar = in.read();
      }
      while (Character.isWhitespace(Char17.last(component)))
        component.setLength(component.length() - 1);
    }
    // Skip following comma
    if (nextChar == separator) nextChar = in.read();
    return (Char17.decode(component.toString()));
  }

  @Override
  protected List<String> internalNext() throws Exception {
    if (nextChar == -1) return (null);
    List<String> line = new ArrayList<String>(columns == null ? 10 : columns.size());
    while (nextChar != -1 && nextChar != 10 && nextChar != 13) {
      String c = component();
      line.add(c);
    }
    // Read new Line
    nextChar = in.read();
    if (nextChar == 13 || nextChar == 10) nextChar = in.read();
    return line;
  }

  /** returns the column names (or NULL)*/
  public List<String> columnNames() {
    return (columns);
  }

  /** returns the number of columns (or NULL)*/
  public Integer numColumns() {
    return (columns == null ? null : columns.size());
  }

  @Override
  public void close() {
    try {
      in.close();
    } catch (Exception e) {
    }
  }

  /** Test method*/
  public static void main(String[] args) throws Exception {
    for (List<String> cols : new CSVLines("./javatools/testdata/CSVTest.csv")) {
      System.out.println(cols);
    }
  }
}
