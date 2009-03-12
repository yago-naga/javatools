package javatools.administrative;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;

import javatools.datatypes.FinalSet;
import javatools.parsers.NumberFormatter;
/** 
This class is part of the Java Tools (see http://mpii.de/~suchanek/downloads/javatools).
It is licensed under the Creative Commons Attribution License 
(see http://creativecommons.org/licenses/by/3.0) by 
Fabian M. Suchanek (see http://mpii.de/~suchanek).
  
If you use the class for scientific purposes, please cite our paper
  Fabian M. Suchanek, Georgiana Ifrim and Gerhard Weikum
  "Combining Linguistic and Statistical Analysis to Extract Relations from Web Documents" (SIGKDD 2006)
     
This class can make progress announcements. The announcements are handled by an object,
but static methods exist to simplify the calls.<BR>
Example:
<PRE>
    Announce.doing("Testing 1");
    Announce.doing("Testing 2");
    Announce.message("Now testing", 3);
    Announce.warning(1,2,3);
    Announce.debug(1,2,3);
    Announce.doing("Testing 3a");
    Announce.doneDoing("Testing 3b");
    Announce.done();
    Announce.progressStart("Testing 3c",5); // 5 steps
    D.waitMS(1000);
    Announce.progressAt(1); // We're at 1 (of 5)
    D.waitMS(3000);
    Announce.progressAt(4); // We're at 4 (of 5)
    D.waitMS(1000);
    Announce.progressDone();
    Announce.done();
    Announce.done();
    Announce.done(); // This is one too much, but it works nevertheless
  -->
    Testing 1...
      Testing 2...
        Now testing 3
        Warning:1 2 3
        Announce.main(243): 1 2 3
        Testing 3a... done
        Testing 3b... done
        Testing 3c...........(4.00 s to go)................................ done (5.00 s)
      done
    done
</PRE>
The progress bar always walks to MAXDOTS dots. The data is written to Announce.out
(by default System.err). The Announcements can be switched on and off, also locally
with setAnnounceLocally/unLocal.
*/
public class Announce implements Closeable {
  /** Maximal number of dots */
  public static int MAXDOTS=40;
  /** Current Announce object */
  public static Announce current=new Announce();

  /** Where to write to (default: System.err) */
  public Writer out=new BufferedWriter(new OutputStreamWriter(System.err));
  /** Switches the writer on or off */
  public boolean on=true;
  /** Indentation level */
  protected int doingLevel=-1;
  /** Are we at the beginning of a line?*/
  protected boolean newLine=true;
  /** Memorizes the maximal value for progressAt(...) */
  protected double progressEnd=0;
  /** Memorizes the number of printed dots */
  protected int progressDots=0;
  /** Memorizes the process start time */
  protected long progressStart=0;
  /** Did we print the estimated time? */
  protected boolean printedEstimatedTime;
  /** Memorizes the timer */
  protected long timer;
  /** TRUE if debugging is on*/
  public static boolean debug;
  
  /** Creates a new Announce object with default settings */
  public Announce() {
  }

  /** Creates a new Announce object with the settings from the given Announce object */
  public Announce(Announce a) {
    out=a.out;
    on=a.on;
    doingLevel=a.doingLevel;
    newLine=a.newLine;
    progressEnd=a.progressEnd;
    progressDots=a.progressDots;
    progressStart=a.progressStart;
    printedEstimatedTime=a.printedEstimatedTime;
  }

  /** Sets the current announcer, returns previous */
  public static Announce setCurrent(Announce a) {
    Announce prev=current;
    current=a;
    return(prev);
  }

  /** Returns the current announcer*/
  public static Announce getCurrent() {
    return(current);
  }

  /** Starts the timer */
  public void startTimerO() {
    timer=System.currentTimeMillis();
  }

  /** Retrieves the time */
  public long getTimeO() {
    return(System.currentTimeMillis()-timer);
  }

  /** Closes the writer */
  public void closeO() throws IOException{
    out.close();
  }

  /** Switches announcing on or off */
  public void setAnnounceO(boolean o) {
    on=o;
  }

  /** Internal printer */
  protected void printO(Object... o) {
    if(!on) return;
    try {
      if(newLine) for(int i=0;i<=doingLevel;i++) out.write("  ");
      if(o==null) out.write("null");
      else {
        for(Object o1 : o) {
          if(o1==null) out.write("null");
          else out.write(o1.toString());
          if(o.length>1) out.write(" ");
        }
      }
      out.flush();
    }
    catch(IOException e) {}
    newLine=false;
  }

  /** Internal printer for new line */
  protected void newLineO() {
    if(!on || newLine) return;
    try {
      out.write("\n");
      out.flush();
    }
    catch(IOException e) {}
    newLine=true;
  }

  /** Prints an (indented) message */
  public void messageO(Object... o) {
    newLineO();
    printO(o);
    newLineO();
  }

  /** Prints a debug message with the class and method name preceeding */
  public void debugO(Object... o) {
    newLineO();
    printO(CallStack.toString(new CallStack().ret().top())+": ");
    printO(o);
    newLineO();
  }

  /** Prints an error message and aborts (even if Announce is off)*/
  public void errorO(Object... o) {
    try {
    out.write("\nError:");
    for(Object s : o) {
      out.write(" ");
      out.write(s.toString());
    }
    out.write("\n");
    out.flush();
    } catch(IOException e) {}
    System.exit(255);
  }

  /** Prints an exception and aborts (even if Announce is off)*/
  public void exceptionO(Exception e, Object... o) {
    try {
    e.printStackTrace(new PrintWriter(out));
    out.write("\n");
    for(Object s : o) {
      out.write(" ");
      out.write(s.toString());
    }
    out.write("\n");
    out.flush();
    } catch(IOException ex) {}
    System.exit(255);
  }

  /** Prints a warning*/
  public void warningO(Object... o) {
    newLineO();
    printO("Warning: ");
    printO(o);
    newLineO();
  }


  /** Sets the writer the data is written to */
  public void setWriterO(Writer w) {
    out=w;
  }

  /** Sets the writer the data is written to */
  public void setWriterO(OutputStream s) {
    out=new BufferedWriter(new OutputStreamWriter(s));
  }

  /** Gets the writer the data is written to (e.g. to close it)*/
  public  Writer getWriterO() {
    return(out);
  }

  /** Writes "s..."*/
  public void doingO(Object... o) {
    newLineO();
    printO(o);
    printO("... ");
    doingLevel++;
  }

  /** Writes "s..."*/
  public void timedDoingO(Object... o) {
    doingO(o);
    startTimerO();
  }
  
  /** Writes "failed NEWLINE" */
  public void failedO() {
    if(doingLevel>=0) {
      doingLevel--;
      printO("failed");
      newLineO();
    }
  }

  /** Writes "done NEWLINE"*/
  public void doneO() {
    if(doingLevel>=0) {
      doingLevel--;
      printO("done");
      newLineO();
    }
  }

  /** Writes "done (time) NEWLINE"*/
  public void timedDoneO() {
    if(doingLevel>=0) {
      doingLevel--;
      printO("done");
      printO(" ("+NumberFormatter.formatMS(getTimeO())+")");
      newLineO();
    }
  }
  
  /** Calls done() and doing(...)*/
  public void doneDoingO(Object... s) {
    doneO();
    doingO(s);
  }

  /** Writes s, prepares to make progress up to max */
  public void progressStartO(String s, double max) {
    progressEnd=max;
    progressDots=0;
    progressStart=System.currentTimeMillis();
    printedEstimatedTime=false;
    newLineO();
    printO(s+"...");
    doingLevel++;
  }

  /** Notes that the progress is at d, prints dots if necessary,
   * calculates and displays the estimated time at 1/10 of the progress */
  public void progressAtO(double d) {
    if(d>progressEnd || d*MAXDOTS/progressEnd<=progressDots) return;
    StringBuilder b=new StringBuilder();    
    while(d*MAXDOTS/progressEnd>progressDots) {
      progressDots++;
      b.append(".");
    }
    if(!printedEstimatedTime && System.currentTimeMillis()-progressStart>20000) {
      b.append('(').append(    
          NumberFormatter.formatMS((long)((System.currentTimeMillis()-progressStart)*(progressEnd-d)/d)))
          .append(" to go)");
      printedEstimatedTime=true;
    }
    printO(b);
  }

  /** Fills missing dots and writes "done NEWLINE"*/
  public void progressDoneO() {
    progressAtO(progressEnd);
    doingLevel--;
    printO(" done ("+NumberFormatter.formatMS(System.currentTimeMillis()-progressStart)+")");
    newLineO();
  }

  /** Writes "failed NEWLINE"*/
  public void progressFailedO() {
    failedO();
  }

  /** Writes a help text and exits */
  public void helpO(String... o) {
    try {
      out.write("\n");
      for(String s : o) {
        out.write(s);
        out.write("\n");
      }
      out.flush();
    } catch(IOException e) {}
    System.exit(63);
  }

  /** Switches announcing on or off */
  public static void setAnnounce(boolean o) {
    current.setAnnounceO(o);
  }

  /** Prints an (indented) message */
  public static void message(Object... o) {
    current.messageO(o);
  }

  /** Prints a debug message with the class and method name preceeding */
  public static void debug(Object... o) {
    if(!debug) return;
    current.newLineO();
    current.printO(CallStack.toString(new CallStack().ret().top())+": ");
    current.printO(o);
    current.newLineO();
  }


  /** Prints an error message and aborts (even if Announce is off)*/
  public static void error(Object... o) {
    current.errorO(o);
  }

  /** Prints an exception and aborts (even if Announce is off)*/
  public static void exception(Exception e, Object... o) {
    current.exceptionO(e, o);
  }

  /** Prints a warning*/
  public static void warning(Object... o) {
    current.warningO(o);
  }


  /** Sets the writer the data is written to */
  public static void setWriter(Writer w) {
    current.setWriterO(w);
  }

  /** Sets the writer the data is written to */
  public static void setWriter(OutputStream s) {
    current.setWriterO(s);
  }

  /** Gets the writer the data is written to (e.g. to close it)*/
  public static Writer getWriter() {
    return(current.getWriterO());
  }

  /** Writes "s..."*/
  public static void doing(Object... o) {
    current.doingO(o);
  }

  /** Writes "s..."*/
  public static void timedDoing(Object... o) {
    current.timedDoingO(o);
  }

  /** Writes "failed NEWLINE" */
  public static void failed() {
    current.failedO();
  }

  /** Writes "done NEWLINE"*/
  public static void done() {
    current.doneO();
  }

  /** Writes "done (time) NEWLINE"*/
  public static void timedDone() {
    current.timedDoneO();
  }

  /** Calls done() and doing(...)*/
  public static void doneDoing(String s) {
    current.doneDoingO(s);
  }

  /** Writes s, prepares to make progress up to max */
  public static void progressStart(String s, double max) {
    current.progressStartO(s, max);
  }

  /** Notes that the progress is at d, prints dots if necessary,
   * calculates and displays the estimated time at 1/10 of the progress */
  public static void progressAt(double d) {
    current.progressAtO(d);
  }

  /** Fills missing dots and writes "done NEWLINE"*/
  public static void progressDone() {
    current.progressDoneO();
  }

  /** Writes "failed NEWLINE"*/
  public static void progressFailed() {
    current.progressFailedO();
  }

  /** Writes a help text and exits */
  public static void help(String... o) {
    current.helpO(o);
  }

  /** Closes the writer */
  public void close() throws IOException{
    current.closeO();
  }

  /** Starts the timer */
  public static void startTimer() {
    current.startTimerO();
  }

  /** Retrieves the time */
  public static long getTime() {
    return(current.getTimeO());
  }

  /** Retrieves the time */
  public void printTimeO() {
    messageO("Time:", NumberFormatter.formatMS(getTimeO()));
  }

  /** Retrieves the time */
  public static void printTime() {
    current.printTimeO();
  }

  protected static final Set<String> helpCommands=new FinalSet<String>("-help","--help","-h","--h","-?","/?","/help");
  
  /** Says whether a command line argument asks for help*/
  public static boolean isHelp(String arg) {
    return(helpCommands.contains(arg.toLowerCase()));
  }
  
  /** Switch debugging*/
  public static void setDebug(boolean b) {
    debug=b;
  }
  
  /** Test routine */
  public static void main(String[] args) {
    Announce.startTimer();
    Announce.doing("Testing 1");
    Announce.doing("Testing 2");
    Announce.message("Now testing", 3);
    Announce.warning(1,2,3);
    Announce.debug(1,2,3);
    Announce.doing("Testing 3a");
    Announce.doneDoing("Testing 3b");
    Announce.done();
    Announce.progressStart("Testing 3c",5); // 5 steps
    D.waitMS(1000);
    Announce.progressAt(1); // We're at 1 (of 5)
    D.waitMS(3000);
    Announce.progressAt(4); // We're at 4 (of 5)
    D.waitMS(1000);
    Announce.progressDone();
    Announce.done();
    Announce.done();
    Announce.done(); // This is one too much, but it works nevertheless
    Announce.printTime();
  }
}