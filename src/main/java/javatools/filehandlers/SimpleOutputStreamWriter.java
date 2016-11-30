package javatools.filehandlers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

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

A SimpleOutputStreamWriter writes the characters directly as bytes to an output stream
-- regardless of the encoding. See SimpleInputStreamReader for an explanation.
*/
public class SimpleOutputStreamWriter extends Writer {

  /** Holds the underlying OutputStrema*/
  public OutputStream out;

  public SimpleOutputStreamWriter(OutputStream o) {
    out = o;
  }

  public SimpleOutputStreamWriter(File f) throws IOException {
    this(new BufferedOutputStream(new FileOutputStream(f)));
  }

  public SimpleOutputStreamWriter(String s) throws IOException {
    this(new File(s));
  }

  @Override
  public void close() throws IOException {
    out.close();
  }

  @Override
  public void flush() throws IOException {
    out.flush();
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    for (int pos = off; pos < off + len; pos++)
      write(cbuf[pos]);
  }

  @Override
  public void write(int c) throws IOException {
    out.write((byte) (c));
  }
}
