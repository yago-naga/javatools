package javatools.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/** 
This class is part of the Java Tools (see http://mpii.de/yago-naga/javatools).
It is licensed under the Creative Commons Attribution License 
(see http://creativecommons.org/licenses/by/3.0) by 
the YAGO-NAGA team (see http://mpii.de/yago-naga)

Some utility methods for arrays
*/
public class FileUtils {
  /**
   * Creates a BufferedReader for UTF-8-encoded files
   * 
   * @param file  File in UTF-8 encoding
   * @return      BufferedReader for file
   * @throws FileNotFoundException
   */
  public static BufferedReader getBufferedUTFReader(File file) throws FileNotFoundException {
    return new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
  }
  
  /**
   * Creates a BufferedReader for UTF-8-encoded files
   * 
   * @param fileName  Path to file in UTF-8 encoding
   * @return      BufferedReader for file
   * @throws FileNotFoundException
   */
  public static BufferedReader getBufferedUTFReader(String fileName) throws FileNotFoundException {
    return new BufferedReader(new InputStreamReader(new FileInputStream(fileName), Charset.forName("UTF-8")));
  }
}
