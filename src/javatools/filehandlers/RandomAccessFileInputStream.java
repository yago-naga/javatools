package javatools.filehandlers;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**This class is part of the Java Tools (see http://mpii.de/~suchanek/downloads/javatools).
  It is licensed under the Creative Commons Attribution License 
  (see http://creativecommons.org/licenses/by/3.0) by 
  Fabian M. Suchanek (see http://mpii.de/~suchanek).
    
  If you use the class for scientific purposes, please cite our paper
    Fabian M. Suchanek, Georgiana Ifrim and Gerhard Weikum
    "Combining Linguistic and Statistical Analysis to Extract Relations from Web Documents" (SIGKDD 2006)

  The class wraps a RandomAccessFile into a Reader
*/

public class RandomAccessFileInputStream extends InputStream {

  protected RandomAccessFile raf;
  
  @Override
  public void close() throws IOException {
    raf.close();
  }
  
  @Override
  public int read() throws IOException {   
    return raf.read();
  }

  public RandomAccessFileInputStream(RandomAccessFile f) {
    raf=f;
  }
}
