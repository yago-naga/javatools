package javatools.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javatools.administrative.Announce;

/** 
 This class is part of the Java Tools (see http://mpii.de/yago-naga/javatools).
 It is licensed under the Creative Commons Attribution License 
 (see http://creativecommons.org/licenses/by/3.0) by 
 the YAGO-NAGA team (see http://mpii.de/yago-naga).
 
 
 


 This class can read characters from a file that is UTF8 encoded.<BR>
 Example:
 <PRE>
 Reader f=new UTF8Reader(new File("blah.blb"));
 int c;
 while((c=f.read())!=-1) System.out.print(Char.normalize(c));
 f.close();
 </PRE>
 */
public class NewUTF8Reader extends Reader {

  /** Holds the input Stream */

  private InputStreamReader in;

  protected BufferedReader bin;

  /** tells whether we want a progress bar*/
  protected boolean progressBar = false;

  /** number of chars for announce */
  protected long numBytesRead = 0;

  private char[] buffer;

  private int position = 0;

  /** Constructs a UTF8Reader from a Reader */
  public NewUTF8Reader(InputStream s) {
    try {
      in = new InputStreamReader(s, "UTF8");
      bin = new BufferedReader(in);
    } catch (UnsupportedEncodingException une) {
      une.printStackTrace();
    }
  }

  /** Constructs a UTF8Reader for an URL 
   * @throws IOException */
  public NewUTF8Reader(URL url) throws IOException {
    this(url.openStream());
  }

  /** Constructs a UTF8Reader from a File */
  public NewUTF8Reader(File f) throws FileNotFoundException {
    this(new FileInputStream(f));
  }

  /** Constructs a UTF8Reader from a File, makes a nice progress bar */
  public NewUTF8Reader(File f, String message) throws FileNotFoundException {
    this(new FileInputStream(f));
    progressBar = true;
    Announce.progressStart(message, f.length());
  }

  /** Constructs a UTF8Reader from a File */
  public NewUTF8Reader(String f) throws FileNotFoundException {
    this(new File(f));
  }

  /** Constructs a UTF8Reader from a File, makes a nice progress bar */
  public NewUTF8Reader(String f, String message) throws FileNotFoundException {
    this(new File(f), message);
  }

  @Override
  public void close() throws IOException {
    if (bin != null) {
      bin.close();
      bin = null;
    }
    if (in != null) {
      in.close();
      in = null;
    }
    if (progressBar) Announce.progressDone();
    progressBar = false;
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    return bin.read(cbuf, off, len);
  }

  @Override
  public int read() throws IOException {
    if (buffer == null || buffer.length == position) {
      String line = readLine();
      if (line != null) {
        buffer = (line + '\n').toCharArray();
        position = 0;
      } else {
        buffer = null;
        position = 0;
        return -1;
      }
    }
    int c = buffer[position];
    position++;
    return c;
  }

  /** Reads a line do not between read() and readLine methods pick one and stick to it.*/
  public String readLine() throws IOException {
    String line = bin.readLine();
    if (progressBar) {
      if (line != null) {
        numBytesRead += line.getBytes().length;
        Announce.progressAt(numBytesRead);
      }
    }
    return line;
  }

  /** Returns the number of bytes read from the underlying stream*/
  public long numBytesRead() {
    return (numBytesRead);
  }

  /** Test method
   * @throws IOException   */
  public static void main(String[] args) throws IOException {
    long time = System.currentTimeMillis();
    String file = "C:/wikipedia/enwiki-100000-test.xml";
    NewUTF8Reader ff = new NewUTF8Reader(new File(file), "Reader wiki file");
    int count = 0;
    int c = -1;
    while ((c = ff.read()) != -1) {
      count++;
    }
    ff.close();
    time = System.currentTimeMillis() - time;
    System.out.println("Done in " + time);

  }
}
