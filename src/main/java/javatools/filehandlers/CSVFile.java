package javatools.filehandlers;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

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

    
  The class allows writing data to a CSV file.
*/
public class CSVFile implements Closeable {

  /** The output writer */
  protected Writer out;

  /** Holds the separator */
  protected String separator = ", ";

  /** quote all components*/
  protected boolean quoteAll = false;

  /** never quote */
  protected boolean quoteNever = false;

  public CSVFile(File f, boolean append, String separator, List<String> columns) throws IOException {
    this.separator = separator;
    if (append && !f.exists()) append = false;
    out = new UTF8Writer(f, append);
    if (append == false && columns != null && columns.size() > 0) {
      out.write("# ");
      write(columns);
    }
  }

  public CSVFile(File f, boolean append, List<String> columns) throws IOException {
    this(f, append, ", ", columns);
  }

  public CSVFile(File f, boolean append, String... columns) throws IOException {
    this(f, append, Arrays.asList(columns));
  }

  public CSVFile(File f, boolean append) throws IOException {
    this(f, append, (List<String>) null);
  }

  public CSVFile(String f, boolean append) throws IOException {
    this(new File(f), append);
  }

  public CSVFile(File f) throws IOException {
    this(f, false);
  }

  public CSVFile(String f) throws IOException {
    this(new File(f));
  }

  public CSVFile(String f, boolean append, List<String> columns) throws IOException {
    this(new File(f), append, columns);
  }

  public CSVFile(String f, boolean append, String... columns) throws IOException {
    this(f, append, Arrays.asList(columns));
  }

  public CSVFile(String f, List<String> columns) throws IOException {
    this(new File(f), false, columns);
  }

  public CSVFile(String f, String... columns) throws IOException {
    this(f, Arrays.asList(columns));
  }

  public CSVFile(File f, List<String> columns) throws IOException {
    this(f, false, columns);
  }

  public CSVFile(File f, String... columns) throws IOException {
    this(f, Arrays.asList(columns));
  }

  /**Sets optional quoting on/off (off by default)*/
  public void setQuoting(boolean q) {
    quoteAll = q;
  }

  /**Sets quoting on/off (off by default)*/
  public void neverQuote(boolean q) {
    quoteNever = q;
  }

  /** Writes the columns to the file*/
  public void write(List<? extends Object> columns) throws IOException {
    for (int i = 0; i < columns.size(); i++) {
      out.write(column(columns.get(i)));
      if (i != columns.size() - 1) out.write(separator);
    }
    out.write("\n");
  }

  /** Writes the columns to the file*/
  public void write(Object... columns) throws IOException {
    write(Arrays.asList(columns));
  }

  /** Formats an entry*/
  protected String column(Object c) {
    String col = c.toString();
    if (quoteNever) return (col);
    if (col.indexOf('"') != -1) {
      return ('"' + col.replaceAll("\"", "\"\"") + '"');
    }
    if (quoteAll || col.matches(".*\\s.*")) return ('"' + col + '"');
    return (col.trim());
  }

  /** Closes the file*/
  @Override
  public void close() {
    try {
      out.close();
    } catch (Exception e) {
    }
  }

  /** Test method*/
  public static void main(String[] args) throws Exception {
    CSVFile out = new CSVFile("c:/fabian/temp/t.csv", "blah", "blub");
    out.write(1, 2, " blah ", "\"blub\"");
    out.close();
  }
}
