package javatools.filehandlers;

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

  protected InputStreamReader in;

  /** number of chars for announce */
  protected long numBytesRead = 0;

  /** tells whether we want a progress bar*/
  protected boolean progressBar = false;

  /** Constructs a UTF8Reader from a Reader */
  public NewUTF8Reader(InputStream s) {
    try {
      in = new InputStreamReader(s, "UTF8");
    } catch (UnsupportedEncodingException une) {
      // "As long as UTF8 is not changed this should never be called";
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
    if (in == null) return;
    in.close();
    in = null;
    if (progressBar) Announce.progressDone();
    progressBar = false;
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    if (in == null) return (-1);
    return in.read(cbuf, off, len);
  }

  @Override
  public int read() throws IOException {
    if (in == null) return -1;
    numBytesRead++;
    if (progressBar) Announce.progressAt(numBytesRead);
    return in.read();
  }

  /** Returns the number of bytes read from the underlying stream*/
  public long numBytesRead() {
    return (numBytesRead);
  }

  /** Reads a line */
  public String readLine() throws IOException {
    if (in == null) return (null);
    StringBuffer sb = new StringBuffer(50);
    int ch = -1;
    char c;
    while ((ch = read()) != -1) {
      c = (char) ch;
      if (c == '\n') {
        return sb.toString();
      } else {
        sb.append(c);
      }
    }
    if (ch == -1 && sb.toString().length() == 0) {
      return null;
    }
    return sb.toString();
  }

  /** Test method
   * @throws IOException   */
  public static void main(String[] args) throws IOException {
    String file = "C:/yagoTest/yago2/means.tsv";
    int step = 10000;
    long time = System.currentTimeMillis();
    UTF8Reader f = new UTF8Reader(new File(file));
    int count = 0;
    System.out.println("Speed test");
    while (count < step * 10 + 2 && f.readLine() != null) {
      count++;
      if (count % step == 0) {
        time = System.currentTimeMillis() - time;
        System.out.println("read " + step + " in " + time + "ms");
        time = System.currentTimeMillis();
      }
    }
    f.close();
    time = System.currentTimeMillis();
    NewUTF8Reader ff = new NewUTF8Reader(new File(file));
    count = 0;
    while (count < step * 10 + 2 && ff.readLine() != null) {
      count++;
      if (count % step == 0) {
        time = System.currentTimeMillis() - time;
        System.out.println("read " + step + " in " + time + "ms");
        time = System.currentTimeMillis();
      }
    }
    ff.close();
    time = System.currentTimeMillis() - time;
    System.out.println("Speed test done, checking if file readers are equal for first " + (step * 100) + " lines");
    f = new UTF8Reader(new File(file));
    ff = new NewUTF8Reader(new File(file));
    count = 0;
    int problem = 0;
    while (count < step * 100) {
      count++;
      String line = ff.readLine();
      String line2 = f.readLine();
      if (line == null) {
        if (line2 != null) {
          System.out.println("null -> does not equal -> " + line2);
        } else {
          break;
        }
      }
      if (!line.equals(line2)) {
        System.out.println(line + " -> does not equal -> " + line2);
        problem++;
      }
    }
    f.close();
    ff.close();
    System.out.println("Done equal check with " + problem + " problems");
  }
}
