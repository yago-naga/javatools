package javatools.filehandlers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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

A SimpleInputStreamReader reads the bytes from an InputStream and passes them
on as characters -- regardless of the encoding.
<BR>
Example:
<PRE>
    // It does not work like this
    Reader r=new InputStreamReader(new ByteArrayInputStream(new byte[]{(byte)144}));
    System.out.println(r.read());
    r.close();
     -----&gt; 65533
     
    // But it does like this
    r=new SimpleInputStreamReader(new ByteArrayInputStream(new byte[]{(byte)144}));
    System.out.println(r.read());
    r.close();
     -----&gt; 144
     
</PRE>
*/

public class SimpleInputStreamReader extends Reader {

  public InputStream in;

  public SimpleInputStreamReader(InputStream i) {
    in = i;
  }

  public SimpleInputStreamReader(File f) throws FileNotFoundException {
    this(new FileInputStream(f));
  }

  public SimpleInputStreamReader(String f) throws FileNotFoundException {
    this(new File(f));
  }

  @Override
  public void close() throws IOException {
    in.close();
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    byte[] bbuf = new byte[len];
    int result = in.read(bbuf, 0, len);
    for (int i = 0; i < result; i++)
      cbuf[off + i] = (char) bbuf[i];
    return result;
  }

  @Override
  public int read() throws IOException {
    return (in.read());
  }

  /**  */
  public static void main(String[] args) throws Exception {
    // It does not work like this
    Reader r = new InputStreamReader(new ByteArrayInputStream(new byte[] { (byte) 144 }));
    System.out.println(r.read());
    r.close();

    // But it does like this
    r = new SimpleInputStreamReader(new ByteArrayInputStream(new byte[] { (byte) 144 }));
    System.out.println(r.read());
    r.close();
  }

}
