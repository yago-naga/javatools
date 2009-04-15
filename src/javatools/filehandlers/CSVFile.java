package javatools.filehandlers;
import javatools.*;
import java.io.*;
import java.util.*;

/**
  This class is part of the Java Tools (see http://mpii.de/yago-naga/javatools).
  It is licensed under the Creative Commons Attribution License 
  (see http://creativecommons.org/licenses/by/3.0) by 
  the YAGO-NAGA team (see http://mpii.de/yago-naga).
    
  The class allows writing data to a CSV file.
*/
public class CSVFile implements Closeable {
  /** The output writer */
  protected Writer out;
  
  public CSVFile(File f, boolean append, List<String> columns) throws IOException {
    if(append && !f.exists()) append=false;
    out=new UTF8Writer(f,append);
    if(append==false && columns!=null && columns.size()>0) {
      out.write("# ");
      write(columns);
    }
  }
  
  public CSVFile(File f, boolean append, String... columns) throws IOException {
    this(f,append,Arrays.asList(columns));
  }
  
  public CSVFile(File f, boolean append) throws IOException {
    this(f,append,(List<String>)null);
  }

  public CSVFile(String f, boolean append) throws IOException {
    this(new File(f),append);
  }

  public CSVFile(File f) throws IOException {
    this(f,false);
  }

  public CSVFile(String f) throws IOException {
    this(new File(f));
  }

  public CSVFile(String f, boolean append, List<String> columns) throws IOException {
    this(new File(f),append,columns);
  }
  
  public CSVFile(String f, boolean append, String... columns) throws IOException {
    this(f,append,Arrays.asList(columns));
  }
  
  public CSVFile(String f, List<String> columns) throws IOException {
    this(new File(f),false,columns);
  }

  public CSVFile(String f, String... columns) throws IOException {
    this(f,Arrays.asList(columns));
  }
  
  public CSVFile(File f, List<String> columns) throws IOException {
    this(f,false,columns);
  }
  
  public CSVFile(File f, String... columns) throws IOException {
    this(f,Arrays.asList(columns));
  }
  
  /** Writes the columns to the file*/
  public void write(List<? extends Object> columns) throws IOException {
    for(int i=0;i<columns.size();i++) {
      out.write(column(columns.get(i)));
      if(i!=columns.size()-1) out.write(", ");   
    }
    out.write("\n");
  }

  /** Writes the columns to the file*/
  public void write(Object... columns) throws IOException {
    write(Arrays.asList(columns));
  }
  
  /** Formats an entry*/
  protected static String column(Object c) {
    String col=c.toString();  
    if(col.indexOf('"')!=-1) {
      return('"'+col.replaceAll("\"","\"\"")+'"');
    }
    if(col.matches(".*\\s.*")) return('"'+col+'"');
    return(col.trim());
  }
  
  /** Closes the file*/
  public void close() {
    try{
    out.close();
    }catch(Exception e){}
  }
  
  /** Test method*/
  public static void main(String[] args) throws Exception {
    CSVFile out = new CSVFile("c:/fabian/temp/t.csv","blah","blub");
    out.write(1,2," blah ","\"blub\"");
    out.close();
  }
}
