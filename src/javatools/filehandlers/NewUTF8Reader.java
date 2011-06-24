package javatools.filehandlers;

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

  private BufferedReader bin;

  /** Constructs a UTF8Reader from a Reader */
  public NewUTF8Reader(InputStream s) {
    try {
      in = new InputStreamReader(s, "UTF8");
      bin = new BufferedReader(in);
    } catch (UnsupportedEncodingException une) {
      une.printStackTrace();
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
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    return bin.read(cbuf, off, len);
  }

  @Override
  public int read() throws IOException {
    return bin.read();
  }

  /** Reads a line */
  public String readLine() throws IOException {
    return bin.readLine();
  }

  /** Test method
   * @throws IOException   */
  public static void main(String[] args) throws IOException {
    String file = "C:/wikipedia/enwiki-100000-test.xml";
    int step = 10000;
    System.out.println("Checking if file readers are equal for first " + (step * 100) + " lines");
    UTF8Reader f = new UTF8Reader(new File(file));
    NewUTF8Reader ff = new NewUTF8Reader(new File(file));
    int count = 0;
    int problem = 0;
    while (count < step * 100) {
      count++;
      String line = ff.readLine();
      String line2 = f.readLine();
      System.out.println(line2);
      if (line == null) {
        if (line2 != null) {
          System.out.println("null -> does not equal -> " + line2);
        } else {
          break;
        }
      }
      if (!line.equals(line2)) {
        System.out.println(count + ": " + line + " -> does not equal -> " + line2);
        problem++;
        break;
      }
    }
    f.close();
    ff.close();
    System.out.println("Done equal check with " + problem + " problems");
  }
}
