package javatools.filehandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

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

This allows to write characters as UTF8 to a file<BR>
Example:
<PRE>
     Writer w=new UTF8Writer("c:\\blah.blb");
     w.write(Char.decodePercentage("Hall&ouml;chen"));
     w.close();
</PRE>
*/
public class UTF8Writer extends Writer {

  /** The real writer */
  protected OutputStream out;

  /** Writes to a file*/
  public UTF8Writer(File f, boolean append) throws IOException {
    this(new FileOutputStream(f, append));
  }

  /** Writes to a file*/
  public UTF8Writer(File f) throws IOException {
    this(f, false);
  }

  /** Writes nowhere*/
  public UTF8Writer() {
  }

  /** Writes to a file*/
  public UTF8Writer(String f) throws IOException {
    this(new File(f));
  }

  /** Writes to a writer*/
  public UTF8Writer(OutputStream f) {
    out = f;
  }

  @Override
  public void close() throws IOException {
    synchronized (lock) {
      if (out != null) out.close();
    }
  }

  @Override
  public void flush() throws IOException {
    synchronized (lock) {
      if (out != null) out.flush();
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    synchronized (lock) {
      for (int i = off; i < off + len; i++)
        write(cbuf[i]);
    }
  }

  @Override
  public void write(int c) throws IOException {

    synchronized (lock) {
      if (out == null) return;
      String s = Char17.encodeUTF8((char) c);
      for (int i = 0; i < s.length(); i++)
        out.write((byte) s.charAt(i));
    }
  }

  /** Writes a line*/
  public void writeln(String s) throws IOException {
    write(s);
    write('\n');
  }

  /** Writes a string*/
  @Override
  public void write(String s) throws IOException {
    for (int i = 0; i < s.length(); i++)
      write(s.charAt(i));
  }

}
