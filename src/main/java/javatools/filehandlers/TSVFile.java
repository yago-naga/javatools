package javatools.filehandlers;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
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

This class iterates over the lines in a TSV file.<BR>
Example:
<PRE>
for(List&lt;String&gt; line : new TSVFile("blah.tsv")) {
  System.out.println(line);
}
</PRE>
*/

public class TSVFile implements Iterable<List<String>>, Iterator<List<String>>, Closeable {

  /** Holds the reader*/
  protected FileLines in;

  public TSVFile(File f) throws IOException {
    in = new FileLines(f);
  }

  public TSVFile(Reader f) throws IOException {
    in = new FileLines(f);
  }

  public TSVFile(File f, String msg) throws IOException {
    in = new FileLines(f, msg);
  }

  public TSVFile(File f, String encoding, String msg) throws IOException {
    in = new FileLines(f, encoding, msg);
  }

  @Override
  public Iterator<List<String>> iterator() {
    return this;
  }

  @Override
  public List<String> next() {
    return Arrays.asList(in.next().split("\t"));
  }

  @Override
  public boolean hasNext() {
    return in.hasNext();
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

  @Override
  public void close() throws IOException {
    in.close();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("Remove on TSVFile");
  }

}
