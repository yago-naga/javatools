package javatools.datatypes;

import java.util.Collection;
import java.util.Iterator;

import javatools.database.Database;


public abstract class StringModifier {
  


  /* Concatenates the Strings contained in an array to a combined string, 
   * separating each two partial Strings with the given delimeter */
  public static String implode(String[] array, String delim){
    if (array.length==0) {
      return "";
    } else {
      StringBuffer sb = new StringBuffer();
      sb.append(array[0]);
      for (int i=1;i<array.length;i++) {
        sb.append(delim);
        sb.append(array[i]);
      }
      return sb.toString();
    }
  }

  /* Concatenates the Strings contained in a collection to a combined string, 
   * separating each two partial Strings with the given delimeter */
  public static String implode(Collection<String> col, String delim){    
    Iterator<String> it=col.iterator();
    if(!it.hasNext())
      return "";
    else{
      StringBuffer sb = new StringBuffer();
      sb.append(it.next());
      while (it.hasNext()){
        sb.append(delim);
        sb.append(it.next());              
      }
      return sb.toString();
    }
  }  
  
  /* Concatenates the Strings contained in a collection to a combined string, 
   * separating each two partial Strings with the given delimeter 
   * while applying the database.format function to each string pieace */
  public static String implodeForDB(Collection<?> col, String delim, Database database ){    
    return implodeForDB(col.iterator(),delim, database);
  }
  
  /* Concatenates the String pieces produced by an iterator to a combined string, 
   * separating each two partial Strings with the given delimeter 
   * while applying the database.format function to each string pieace */
  public static String implodeForDB(Iterator<?> it, String delim, Database database ){        
    if(!it.hasNext())
      return "";
    else{
      StringBuffer sb = new StringBuffer();
      sb.append(database.format(it.next()));
      while (it.hasNext()){
        sb.append(delim);
        sb.append(database.format(it.next()));              
      }
      return sb.toString();
    }
  }
  
  
  /** limits the length of a String to the given size
   * ie applies s.substring(0,length) for given length iff
   * length<s.length() */
  public static String limitLength(String s, int length){
    if(s.length()>length)
      return s.substring(0,length);
    else return s;
  }
  

     
  

}
