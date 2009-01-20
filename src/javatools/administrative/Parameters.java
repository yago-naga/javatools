package javatools.administrative;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javatools.datatypes.FinalSet;
import javatools.filehandlers.DeepFileSet;
import javatools.filehandlers.FileLines;

/** 
This class is part of the Java Tools (see http://mpii.de/~suchanek/downloads/javatools).
It is licensed under the Creative Commons Attribution License 
(see http://creativecommons.org/licenses/by/3.0) by 
Fabian M. Suchanek (see http://mpii.de/~suchanek).
  
If you use the class for scientific purposes, please cite our paper
  Fabian M. Suchanek, Georgiana Ifrim and Gerhard Weikum
  "Combining Linguistic and Statistical Analysis to Extract Relations from Web Documents" (SIGKDD 2006)

Provides an interface for an ini-File. The ini-File may contain parameters of the form
<PRE>
parameterName = value
...
</PRE>
It may also contain comments or section headers (i.e. anything that does not match the
above pattern). Parameter names are not case sensitive. Initial and terminal spaces
are trimmed for both parameter names and values. Boolean parameters accept multiple
ways of expressing "true" (namely "on", "true", "yes" and "active").<P>

To avoid passing around object handles, this class does not function as an object!
There is only one "static object". Example:
<PRE>
  // Read data from my.ini
  Parameters.init("my.ini");
  // Abort with error message if the following parameters are not specified
  Parameters.ensureParameters(
     "firstPar - some help text for the first parameter",
     "secondPar - some help text for the secondparameter"
  );
  // Retrieve the value of a parameter
  String p=Parameters.get("firstPar");
</PRE>
You can load parameters from multiple files. These will overlay.
*/
public class Parameters {
  /** Thrown for an undefined Parameter */
  public static class UndefinedParameterException extends RuntimeException {
    public UndefinedParameterException(String s, File f) {
      super("The parameter "+s+" is undefined in "+f);
    }
  }
  /** Holds the filename of the ini-file */
  public static File iniFile=null;

  /** Contains the values for the parameters*/
  public static Map<String,String> values=null;

  /** Holds the pattern used for ini-file-entries */
  public static Pattern INIPATTERN=Pattern.compile(" *(\\w+) *= *(.*) *");

  /** Holds words that count as "yes" for boolean parameters */
  public static FinalSet<String> yes=new FinalSet<String>(new String [] {
        "active",
        "on",
        "true",
        "yes"
  });

  /** Returns a value for a file or folder parameter */
  public static File getFile(String s) throws UndefinedParameterException {
    return(new File(get(s)));
  }
  
  /** Returns a value for a boolean parameter */
  public static boolean getBoolean(String s) throws UndefinedParameterException  {
    String v=get(s);
    return(yes.contains(v.toLowerCase()));
  }

  /** Returns a value for a boolean parameter, returning a default value by default */
  public static boolean getBoolean(String s, boolean defaultValue) {
    String v=get(s,defaultValue?"yes":"no");
    return(yes.contains(v.toLowerCase()));
  }

  /** Returns a value for a parameter*/
  public static String get(String s) throws UndefinedParameterException  {
    if(values==null) throw new RuntimeException("Call init() before get()!");
    String pname=s.indexOf(' ')==-1?s:s.substring(0,s.indexOf(' '));
    String v=values.get(pname.toLowerCase());
    if(v==null) throw new UndefinedParameterException(s,iniFile);
    return(v);
  }

  /** Returns a value for a parameter, returning a default value by default */
  public static String get(String s, String defaultValue)  {
    if(values==null) throw new RuntimeException("Call init() before get()!");
    String pname=s.indexOf(' ')==-1?s:s.substring(0,s.indexOf(' '));
    String v=values.get(pname.toLowerCase());
    if(v==null) return(defaultValue);
    return(v);
  }

  /** Initializes the parameters from a file*/
  public static void init(File f) throws IOException {
    if(f.equals(iniFile)) return;
    values=new TreeMap<String,String>();
    iniFile=f;
    for(String l : new FileLines(iniFile)) {
      Matcher m=INIPATTERN.matcher(l);
      if(!m.matches()) continue;
      String s=m.group(2).trim();
      if(s.startsWith("\"")) s=s.substring(1);
      if(s.endsWith("\"")) s=s.substring(0,s.length()-1);
      values.put(m.group(1).toLowerCase(),s);
    }
  }
  
  /** Seeks the file in all subfolders of the current folder
   * and initializes*/
  public static void initAnywhere(String filename) throws IOException {
    initAnywhere(new File("."),filename);
  }
  
  /** Seeks the file (possibly given by wildcards) in all subfolders
   * and initializes*/
  public static void initAnywhere(File folder, String wildcard) throws IOException {
    Iterator<File> i=new DeepFileSet(folder,wildcard);
    if(!i.hasNext()) throw new FileNotFoundException(folder+":"+wildcard);
    File f=i.next();
    if(i.hasNext()) throw new FileNotFoundException("INI file occurs twice in folder tree "+folder+":"+wildcard);
    init(f);
  }
  
  /** Tells whether a parameter is defined */
  public static boolean isDefined(String s) {
    if(values==null) throw new RuntimeException("Call init() before get()!");
    String pname=s.indexOf(' ')==-1?s:s.substring(0,s.indexOf(' '));
    return(values.containsKey(pname.toLowerCase()));
  }
  
  /** Initializes the parameters from a file*/
  public static void init(String file) throws IOException {
    init(new File(file));
  }
  
  /** Reports an error message and aborts if the parameters are undefined.
   * p may contain strings of the form "parametername explanation"*/
  public static void ensureParameters(String... p) {
    if(values==null) throw new RuntimeException("Call init() before ensureParameters()!");
    boolean OK=true;
    for(String s : p) {
      String pname=s.indexOf(' ')==-1?s:s.substring(0,s.indexOf(' '));
      try {
        get(pname);
      } catch(Exception e) {
        if(OK) System.err.println("\n\nError: The following parameters are undefined in "+iniFile);
        System.err.println("    "+s);
        OK=false;
      }
    }
    if(OK) return;
    System.exit(255);
  }

  /** Parses the arguments of the main method and tells whether a parameter is on or off */
  public static boolean getBooleanArgument(String[] args,String... argnames) {
    String arg=" ";
    for(String s : args) arg+=s+' ';
    String p="\\W(";
    for(String s : argnames) p+=s+'|';
    if(p.endsWith("|")) p=p.substring(0, p.length()-1);
    p+=")\\W";
    Matcher m=Pattern.compile(p).matcher(arg);
    if(!m.find()) return(false);
    String next=arg.substring(m.end()).toLowerCase();
    if(next.indexOf(' ')!=-1) next=next.substring(0, next.indexOf(' '));
    if(next.equals("off")) return(false);    
    if(next.equals("0")) return(false);    
    if(next.equals("false")) return(false);
    String previous=arg.substring(0,m.start()).toLowerCase();
    if(previous.indexOf(' ')!=-1) previous=previous.substring(previous.lastIndexOf(' ')+1);    
    if(previous.equals("no")) return(false);
    return(true);
  }
  
  /** Deletes all current values*/
  public static void reset() {
    iniFile=null;
    values=null;
  }
  
  /** Test routine */
  public static void main(String argv[]) throws Exception {
    System.err.println("Enter the name of an ini-file: ");
    init(D.r());
    D.p(values);
  }
}
