package javatools.filehandlers;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;

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

This writer forwards to multiple writers*/

public class MultiWriter extends Writer {

  protected Collection<Writer> writers;

  public MultiWriter(Collection<Writer> writers) {
    this.writers = writers;
  }

  public MultiWriter(Writer... writers) {
    this(Arrays.asList(writers));
  }

  @Override
  public void close() throws IOException {
    for (Writer w : writers)
      w.close();
  }

  @Override
  public void flush() throws IOException {
    for (Writer w : writers)
      w.flush();
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    for (Writer w : writers)
      w.write(cbuf, off, len);
  }

  @Override
  public void write(int c) throws IOException {
    for (Writer w : writers)
      w.write(c);
  }

  @Override
  public void write(String str) throws IOException {
    for (Writer w : writers)
      w.write(str);
  }
}
